package com.inepex.nyomagestreamprocessor.testapp.common.incomingnyomstreambuilder

import com.inepex.nyomagestreamprocessor.api.incomingnyom.EventsOuterClass
import com.inepex.nyomagestreamprocessor.api.incomingnyom.IncomingNyomEntryOuterClass
import com.inepex.nyomagestreamprocessor.api.incomingnyom.LocationOuterClass
import com.inepex.nyomagestreamprocessor.api.incomingnyom.StatusOuterClass
import com.inepex.nyomagestreamprocessor.nyompipeline.mapincomingnyom.Constants
import com.inepex.nyomagestreamprocessor.testapp.cases.latE5
import com.inepex.nyomagestreamprocessor.testapp.cases.lonE5
import org.geotools.referencing.GeodeticCalculator
import java.time.Duration

class IncomingNyomStreamBuilder(private val nativeId: String) {

    private val stream = mutableListOf<IncomingNyomEntryOuterClass.IncomingNyomEntry>()

    fun getStream() = stream

    fun location(location: LocationOuterClass.Location): IncomingNyomStreamBuilder {
        stream.add(IncomingNyomEntryOuterClass.IncomingNyomEntry.newBuilder().apply {
            nativeId = this@IncomingNyomStreamBuilder.nativeId
            this.location = location
        }.build())
        return this
    }

    fun movement(durationDiffSeconds: Long, latDiffMeter: Int = 0, lonDiffMeter: Int = 0): IncomingNyomStreamBuilder {
        stream.add(IncomingNyomEntryOuterClass.IncomingNyomEntry.newBuilder().apply {
            nativeId = this@IncomingNyomStreamBuilder.nativeId
            location = lastLocation().let { lastLocation ->
                GeodeticCalculator().apply {
                    setStartingGeographicPoint(lastLocation.lon / Constants.E5,
                            lastLocation.lat / Constants.E5)
                    setDirection(0.0, latDiffMeter.toDouble())
                }.let { latMovementResult ->
                    GeodeticCalculator().apply {
                        startingGeographicPoint = latMovementResult.destinationGeographicPoint
                        setDirection(90.0, lonDiffMeter.toDouble())
                    }.let { bothMovementResult ->
                        LocationOuterClass.Location.newBuilder()
                                .setTimestamp(lastLocation.timestamp + Duration.ofSeconds(durationDiffSeconds).toMillis())
                                .latE5(bothMovementResult.destinationGeographicPoint.y)
                                .lonE5(bothMovementResult.destinationGeographicPoint.x)
                                .build()
                    }
                }
            }
        }.build())
        return this
    }

    fun event(event: EventsOuterClass.Events): IncomingNyomStreamBuilder {
        stream.add(IncomingNyomEntryOuterClass.IncomingNyomEntry.newBuilder().apply {
            nativeId = this@IncomingNyomStreamBuilder.nativeId
            this.events = event
        }.build())
        return this
    }

    fun status(status: StatusOuterClass.Status): IncomingNyomStreamBuilder {
        stream.add(IncomingNyomEntryOuterClass.IncomingNyomEntry.newBuilder().apply {
            nativeId = this@IncomingNyomStreamBuilder.nativeId
            this.status = status
        }.build())
        return this
    }

    private fun lastLocation(): LocationOuterClass.Location {
        return stream.last { it.entryCase == IncomingNyomEntryOuterClass.IncomingNyomEntry.EntryCase.LOCATION }.location
    }
}
