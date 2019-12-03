package com.inepex.nyomagestreamprocessor.trippipeline.maptotrip

import com.inepex.nyomagestreamprocessor.api.generatednyom.NyomOuterClass.Nyom
import com.inepex.nyomagestreamprocessor.pipeline.Dependencies
import com.inepex.nyomagestreamprocessor.schema.elastic.GeoPoint
import com.inepex.nyomagestreamprocessor.schema.elastic.Mapping
import com.inepex.nyomagestreamprocessor.schema.elastic.trip.Trip
import com.inepex.nyomagestreamprocessor.trippipeline.context.Step1_NyomKafkaSource
import com.inepex.nyomagestreamprocessor.trippipeline.context.Step2_MapToTrip
import org.apache.flink.api.common.state.ValueState
import org.apache.flink.api.common.state.ValueStateDescriptor
import org.apache.flink.api.common.typeinfo.TypeHint
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.util.Collector
import org.geotools.geometry.jts.WKTWriter2
import org.geotools.referencing.GeodeticCalculator
import org.locationtech.jts.geom.Coordinate
import java.time.Duration

class MapToTripProcessFunction : KeyedProcessFunction<Long, Step1_NyomKafkaSource, Step2_MapToTrip>() {

    @Transient
    private lateinit var unprocessedNyomsState: ValueState<MutableList<Nyom>>

    @Transient
    private lateinit var latestTrip: ValueState<Trip>

    override fun open(parameters: Configuration?) {
        unprocessedNyomsState = runtimeContext.getState(ValueStateDescriptor<MutableList<Nyom>>(
                "unprocessedNyoms", TypeInformation.of(object : TypeHint<MutableList<Nyom>>() {})))
        latestTrip = runtimeContext.getState(ValueStateDescriptor<Trip>(
                "latestTrip", TypeInformation.of(object : TypeHint<Trip>() {})))
    }

    override fun processElement(value: Step1_NyomKafkaSource, ctx: Context, out: Collector<Step2_MapToTrip>) {
        val nyoms = unprocessedNyomsState.value() ?: mutableListOf()
        nyoms.add(value.nyom)
        unprocessedNyomsState.update(nyoms)

        StringBuilder().let {
            processHelper(value.traceId, nyoms, ctx, out, it)
            if (it.length > 0) {
                Dependencies.get().logger.info("MapToTrip.processElement, traceId: ${value.traceId}\n$it")
            }
        }

    }

    private fun processHelper(traceId: String, nyoms: MutableList<Nyom>, ctx: Context, out: Collector<Step2_MapToTrip>, debugInfo: StringBuilder) {
        if (validRoute(nyoms)) {
            debugInfo.appendln("nyoms.size: ${nyoms.size}, validRoute")
            longestMovementStartStopIndex(nyoms).let { actualMovementStartStopIndex ->
                debugInfo.appendln("actualMovementStartStopIndex: $actualMovementStartStopIndex")
                if (actualMovementStartStopIndex.second == 0) {
                    // don't stuck when the start of the nyoms aren't a movement
                    // if interval > x and still not a movement, drop nyoms
                    if (atLeastMinInterval(nyoms, Constants.MOVEMENT_MINIMAL_INTERVAL_SECONDS)) {
                        debugInfo.appendln("not a movement")
                        unprocessedNyomsState.update(mutableListOf())
                    }
                } else {
                    findNextMovement(nyoms, actualMovementStartStopIndex.second + 1).let { nextMovementStartIndex ->
                        debugInfo.appendln("nextMovementStartIndex: $nextMovementStartIndex")
                        if (nextMovementStartIndex != -1) {
                            val actualStop = nyoms[actualMovementStartStopIndex.second]
                            val nextStart = nyoms[nextMovementStartIndex]
                            val closeEnoughToJoin = isCloseEnoughToJoin(actualStop, nextStart)
                            if (!continous(nyoms.subList(actualMovementStartStopIndex.second, nextMovementStartIndex + 1))) {
                                when {
                                    closeEnoughToJoin && !isJumpInTime(actualStop, nextStart) -> {
                                        debugInfo.appendln("!continous && closeEnoughToJoin && !isJumpInTime")
                                        // drop invalid positions
                                        nyoms.filterIndexed { index, _ ->
                                            index <= actualMovementStartStopIndex.second ||
                                                    index >= nextMovementStartIndex
                                        }.toMutableList().let {
                                            processHelper(traceId, it, ctx, out, debugInfo)
                                        }
                                    }
                                    closeEnoughToJoin && isJumpInTime(actualStop, nextStart) -> {
                                        debugInfo.appendln("!continous && closeEnoughToJoin && isJumpInTime")
                                        createTripAndUpdateState(traceId, ctx, out, nyoms, actualMovementStartStopIndex,
                                                nextMovementStartIndex, true, true)
                                    }
                                    else -> {
                                        debugInfo.appendln("!continous && !closeEnoughToJoin")
                                        createTripAndUpdateState(traceId, ctx, out, nyoms, actualMovementStartStopIndex,
                                                nextMovementStartIndex, false, false)
                                    }
                                }
                            } else {
                                debugInfo.appendln("continous, closeEnoughToJoin: $closeEnoughToJoin")
                                createTripAndUpdateState(traceId, ctx, out, nyoms, actualMovementStartStopIndex,
                                        nextMovementStartIndex, true, closeEnoughToJoin)
                            }
                        }
                    }
                }
            }
        } else {
            // don't stuck when the start of the nyoms aren't valid
            // if interval > x and still not a valid route, drop nyoms
            if (atLeastMinInterval(nyoms, Constants.MOVEMENT_MINIMAL_INTERVAL_SECONDS)) {
                debugInfo.appendln("invalid route")
                unprocessedNyomsState.update(mutableListOf())
            }
        }
    }

    private fun validRoute(nyoms: MutableList<Nyom>): Boolean {
        return atLeastMinInterval(nyoms, Constants.MOVEMENT_MINIMAL_INTERVAL_SECONDS)
                && firstIntervalWithAtLeastMinLength(nyoms, Constants.MOVEMENT_MINIMAL_INTERVAL_SECONDS).let { continous(it) }
    }

    private fun isMovement(nyoms: MutableList<Nyom>): Boolean {
        return atLeastMinInterval(nyoms, Constants.MOVEMENT_MINIMAL_INTERVAL_SECONDS)
                && continous(nyoms) && moving(nyoms) && !startsWithWait(nyoms) && !endsWithWait(nyoms)
    }


    private fun longestMovementStartStopIndex(nyoms: MutableList<Nyom>): Pair<Int, Int> =
            endIndexOfFirstIntervalWithAtLeastMinLength(nyoms, Constants.MOVEMENT_MINIMAL_INTERVAL_SECONDS).let { firstIntervalEndIndex ->
                var i = 0
                var nyomsSublist: MutableList<Nyom>
                var endIndex = 0
                var endsWithWait = false
                do {
                    endIndex = firstIntervalEndIndex + i
                    nyomsSublist = nyoms.subList(0, endIndex + 1)
                    i++
                    endsWithWait = endsWithWait(nyomsSublist)
                } while (isMovement(nyomsSublist) && endIndex + 1 < nyoms.size)
                if (endsWithWait) {
                    val lengthOfWait = lastMinInterval(nyomsSublist, Constants.WAIT_MIN_INTERVAL_SECONDS).size
                    0 to Math.max(0, endIndex - lengthOfWait + 1)
                } else {
                    0 to endIndex - 1
                }
            }

    private fun findNextMovement(nyoms: MutableList<Nyom>, startIndex: Int): Int {
        for (i in startIndex until nyoms.size) {
            if (isMovement(nyoms.subList(i, nyoms.size))) {
                return i
            }
        }
        return -1
    }

    private fun createTripAndUpdateState(traceId: String, ctx: Context, out: Collector<Step2_MapToTrip>,
                                         nyoms: MutableList<Nyom>, actualMovementStartStopIndex: Pair<Int, Int>,
                                         nextMovementStartIndex: Int, withWait:Boolean, shouldJoinStopStartLocation: Boolean) {
        val trip = createTrip(ctx.currentKey, nyoms, actualMovementStartStopIndex,
                nextMovementStartIndex,
                withWait,
                shouldJoinStopStartLocation)
        listDropInPlace(nyoms, nextMovementStartIndex)
        unprocessedNyomsState.update(nyoms)
        out.collect(Step2_MapToTrip(traceId, trip))
    }

    private fun endsWithWait(nyoms: MutableList<Nyom>): Boolean {
        return atLeastMinInterval(nyoms, Constants.WAIT_MIN_INTERVAL_SECONDS)
                && !moving(lastMinInterval(nyoms, Constants.WAIT_MIN_INTERVAL_SECONDS))
    }

    private fun lastMinInterval(nyoms: MutableList<Nyom>, minInterval: Int): List<Nyom> {
        return nyoms.asReversed().indexOfFirst {
            Duration.ofMillis(nyoms.last().timestampMillis - it.timestampMillis).seconds >= minInterval
        }.let {
            nyoms.subList(nyoms.size - 1 - it, nyoms.size)
        }
    }

    private fun startsWithWait(nyoms: MutableList<Nyom>): Boolean {
        return atLeastMinInterval(nyoms, Constants.WAIT_MIN_INTERVAL_SECONDS)
                && !moving(firstIntervalWithAtLeastMinLength(nyoms, Constants.WAIT_MIN_INTERVAL_SECONDS))
    }

    private fun atLeastMinInterval(nyoms: MutableList<Nyom>, minInterval: Int): Boolean {
        return Duration.ofMillis(nyoms.last().timestampMillis - nyoms.first().timestampMillis).seconds >= minInterval
    }

    private fun firstIntervalWithAtLeastMinLength(nyoms: MutableList<Nyom>, minInterval: Int): List<Nyom> {
        return nyoms.subList(0, endIndexOfFirstIntervalWithAtLeastMinLength(nyoms, minInterval) + 1)
    }

    private fun endIndexOfFirstIntervalWithAtLeastMinLength(nyoms: MutableList<Nyom>, minInterval: Int) =
            nyoms.indexOfFirst {
                Duration.ofMillis(it.timestampMillis -
                        nyoms.first().timestampMillis).seconds >= minInterval
            }


    private fun continous(filteredNyoms: List<Nyom>): Boolean {
        filteredNyoms.forEachIndexed { index, nyom ->
            if (index > 0) {
                val prevNyom = filteredNyoms[index - 1]
                val intervalSeconds = Duration.ofMillis(nyom.timestampMillis - prevNyom.timestampMillis).toMillis() / 1000.0
                val distanceMeters = GeodeticCalculator().apply {
                    setStartingGeographicPoint(prevNyom.location.coordinates.lon, prevNyom.location.coordinates.lat)
                    setDestinationGeographicPoint(nyom.location.coordinates.lon, nyom.location.coordinates.lat)
                }.orthodromicDistance
                val speed = distanceMeters / intervalSeconds

                if ((distanceMeters > Constants.CONTINOUS_LOCATION_EXTREME_SMALL_DISTANCE_METERS
                        && intervalSeconds > Constants.CONTINOUS_LOCATION_EXTREME_SMALL_INTERVAL_SECONDS
                        && speed > Constants.CONTINOUS_LOCATION_SPEED_THRESHOLD_METERPERSEC)
                        || distanceMeters > Constants.CONTINOUS_LOCATION_DISTANCE_THRESHOLD_METERS
                        || intervalSeconds > Constants.CONTINOUS_LOCATION_INTERVAL_THRESHOLD_SECONDS
                ) {
                    return false
                }
            }
        }
        return true
    }

    private fun moving(filteredNyoms: List<Nyom>) =
            filteredNyoms.fold(filteredNyoms[0] to 0.0) { previousNyomAndsumDistance, nyom ->
                nyom to GeodeticCalculator().apply {
                    setStartingGeographicPoint(previousNyomAndsumDistance.first.location.coordinates.lon, previousNyomAndsumDistance.first.location.coordinates.lat)
                    setDestinationGeographicPoint(nyom.location.coordinates.lon, nyom.location.coordinates.lat)
                }.orthodromicDistance + previousNyomAndsumDistance.second
            }.second.let { sumDistanceMeters ->
                val intervalSeconds = Duration.ofMillis(filteredNyoms.last().timestampMillis - filteredNyoms.first().timestampMillis).toMillis() / 1000.0
                val speed = sumDistanceMeters / intervalSeconds

                speed > Constants.MOVEMENT_SPEED_THRESHOLD_METERPERSEC
            }

    private fun isCloseEnoughToJoin(nyom1: Nyom, nyom2: Nyom): Boolean =
            GeodeticCalculator().apply {
                setStartingGeographicPoint(nyom1.location.coordinates.lon, nyom1.location.coordinates.lat)
                setDestinationGeographicPoint(nyom2.location.coordinates.lon, nyom2.location.coordinates.lat)
            }.orthodromicDistance < Constants.MOVEMENT_JOIN_MAX_DISTANCE_METERS

    private fun isJumpInTime(nyom1: Nyom, nyom2: Nyom): Boolean =
            Duration.ofMillis(nyom2.timestampMillis - nyom1.timestampMillis).toMillis() / 1000.0 > Constants.CONTINOUS_LOCATION_INTERVAL_THRESHOLD_SECONDS

    private fun createTrip(deviceId: Long, nyoms: MutableList<Nyom>, actualMovementStartStopIndex: Pair<Int, Int>,
                           nextMovementStart: Int, withWait: Boolean, shouldJoinStopStartLocation: Boolean): Trip {
        return Trip(
                deviceId,
                nyoms[actualMovementStartStopIndex.first].let {
                    GeoPoint(it.location.coordinates.lat,
                            it.location.coordinates.lon)
                },
                nyoms[if (shouldJoinStopStartLocation) nextMovementStart else
                    actualMovementStartStopIndex.second].let {
                    GeoPoint(it.location.coordinates.lat,
                            it.location.coordinates.lon)
                },
                nyoms[actualMovementStartStopIndex.first].timestampMillis,
                nyoms[actualMovementStartStopIndex.second].timestampMillis,
                if (withWait) nyoms[nextMovementStart].timestampMillis - nyoms[actualMovementStartStopIndex.second].timestampMillis else 0,
                getRoute(nyoms, actualMovementStartStopIndex, nextMovementStart, shouldJoinStopStartLocation),
                getRoutePointTimestamps(),
                nyoms[actualMovementStartStopIndex.first].mapping.let { Mapping(it.objectId1, it.objectId2, it.objectId3) }
        )
    }

    private fun getRoute(nyoms: MutableList<Nyom>, actualMovementStartStopIndex: Pair<Int, Int>,
                         nextMovementStart: Int, shouldJoinStopStartLocation: Boolean): String {
        return nyoms.subList(actualMovementStartStopIndex.first, actualMovementStartStopIndex.second + 1).toMutableList().let {
            if (shouldJoinStopStartLocation) {
                it.add(nyoms[nextMovementStart])
            }
            it
        }.map {
            it.location.coordinates.let { Coordinate(it.lon, it.lat) } // elastic uses lon, lat order
        }.let {
            WKTWriter2.toLineString(it.toTypedArray())
        }
    }

    private fun getRoutePointTimestamps(): List<Long> {
        return emptyList()
    }


    private fun listDropInPlace(list: MutableList<Nyom>, n: Int) {
        for (i in 0 until n) {
            list.removeAt(0)
        }
    }
}
