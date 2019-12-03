package com.inepex.nyomagestreamprocessor.nyompipeline.mapincomingnyom

import com.inepex.nyomagestreamprocessor.api.incomingnyom.StatusOuterClass
import com.inepex.nyomagestreamprocessor.api.incomingnyom.EventsOuterClass
import com.inepex.nyomagestreamprocessor.api.incomingnyom.IncomingNyomEntryOuterClass
import com.inepex.nyomagestreamprocessor.schema.elastic.*
import com.inepex.nyomagestreamprocessor.schema.elastic.nyom.*

class MapIncomingNyomService {

    fun execute(
            timestamp: Long,
            deviceId: Long,
            mapping: Mapping,
            latestNyom: Nyom?,
            batchOfProcesableEntries: List<IncomingNyomEntryOuterClass.IncomingNyomEntry>,
            shouldBeGroupedWithLatestNyom: Boolean

    ): Result {
        val execution = MapIncomingNyomExecution(timestamp, deviceId, mapping, latestNyom,
                batchOfProcesableEntries, shouldBeGroupedWithLatestNyom)
        return execution.getResult()
    }

    class MapIncomingNyomExecution(
            private val timestamp: Long,
            deviceId: Long,
            mapping: Mapping,
            private val latestNyom: Nyom?,
            private val batchOfProcesableEntries: List<IncomingNyomEntryOuterClass.IncomingNyomEntry>,
            private val shouldBeGroupedWithLatestNyom: Boolean
    ) {

        private val newNyom: Nyom
        private var numberOfSkippedLocations: Int = 0
        private var numberOfSkippedEvents: Int = 0
        private var numberOfSkippedStatuses: Int = 0

        init {
            newNyom = Nyom(deviceId = deviceId, timestampMillis = timestamp,
                    location = newLocation(), event = newEvent(), status = newStatus(), mapping = mapping)
        }

        private fun newLocation(): Location {
            val lastLocation = batchOfProcesableEntries.lastOrNull { it.entryCase == IncomingNyomEntryOuterClass.IncomingNyomEntry.EntryCase.LOCATION }
            return when {
                lastLocation != null -> {
                    batchOfProcesableEntries.filter { it.entryCase == IncomingNyomEntryOuterClass.IncomingNyomEntry.EntryCase.LOCATION }.size.apply {
                        if (this > 1) numberOfSkippedLocations += this - 1
                    }
                    lastLocation.location.let {
                        Location(GeoPoint(it.lat / Constants.E5,
                                it.lon / Constants.E5),
                                it.altitudeMeters,
                                it.headingDegrees,
                                it.speedDMPS,
                                it.accuracyMeter,
                                false
                        )
                    }
                }
                latestNyom != null -> latestNyom.location.copy(interpolated = !shouldBeGroupedWithLatestNyom)
                else -> Location()
            }
        }

        private fun newEvent(): Event {
            val sos = getEvent(EventsOuterClass.Events::getSos)
            val userTurnOn = getEvent(EventsOuterClass.Events::getUserTurnOn)
            val userTurnOff = getEvent(EventsOuterClass.Events::getUserTurnOff)
            val sleepStateChange = getEvent(EventsOuterClass.Events::getSleepStateChange)
            val trafficLimitExceeded = getEvent(EventsOuterClass.Events::getTrafficLimitExceeded)
            val haveNet = getEvent(EventsOuterClass.Events::getHaveNet)
            val netLost = getEvent(EventsOuterClass.Events::getNetLost)
            val haveGps = getEvent(EventsOuterClass.Events::getHaveGps)
            val gpsLost = getEvent(EventsOuterClass.Events::getGpsLost)
            val wifiOn = getEvent(EventsOuterClass.Events::getWifiOn)
            val wifiOff = getEvent(EventsOuterClass.Events::getWifiOff)
            val cellularNetOn = getEvent(EventsOuterClass.Events::getCellularNetOn)
            val cellularNetOff = getEvent(EventsOuterClass.Events::getCellularNetOff)
            val locationCollectionStarted = getEvent(EventsOuterClass.Events::getLocationCollectionStarted)
            val locationCollectionStopped = getEvent(EventsOuterClass.Events::getLocationCollectionStopped)
            val wayPoint = getEvent(EventsOuterClass.Events::getWaypoint)
            val tripStart = getEvent(EventsOuterClass.Events::getTripStart)
            val tripStop = getEvent(EventsOuterClass.Events::getTripStop)
            val turnOnValue = getEvent(EventsOuterClass.Events::getTurnOnValue)

            return Event(
                    sos = sos,
                    latestSosTimestamp = getEventTimestamp(sos, latestNyom?.event?.latestSosTimestamp),
                    userTurnOn = userTurnOn,
                    latestUserTurnOn = getEventTimestamp(userTurnOn, latestNyom?.event?.latestUserTurnOn),
                    userTurnOff = userTurnOff,
                    latestUserTurnOff = getEventTimestamp(userTurnOff, latestNyom?.event?.latestUserTurnOff),
                    sleepStateChange = sleepStateChange,
                    latestSleepStateChange = getEventTimestamp(sleepStateChange, latestNyom?.event?.latestSleepStateChange),
                    trafficLimitExceeded = trafficLimitExceeded,
                    latestTrafficLimitExceeded = getEventTimestamp(trafficLimitExceeded, latestNyom?.event?.latestTrafficLimitExceeded),
                    haveNet = haveNet,
                    latestHaveNet = getEventTimestamp(haveNet, latestNyom?.event?.latestHaveNet),
                    netLost = netLost,
                    latestNetLost = getEventTimestamp(netLost, latestNyom?.event?.latestNetLost),
                    haveGps = haveGps,
                    latestHaveGps = getEventTimestamp(haveGps, latestNyom?.event?.latestHaveGps),
                    gpsLost = gpsLost,
                    latestGpsLost = getEventTimestamp(gpsLost, latestNyom?.event?.latestGpsLost),
                    wifiOn = wifiOn,
                    latestWifiOn = getEventTimestamp(wifiOn, latestNyom?.event?.latestWifiOn),
                    wifiOff = wifiOff,
                    latestWifiOff = getEventTimestamp(wifiOff, latestNyom?.event?.latestWifiOff),
                    cellularNetOn = cellularNetOn,
                    latestCellularNetOn = getEventTimestamp(cellularNetOn, latestNyom?.event?.latestCellularNetOn),
                    cellularNetOff = cellularNetOff,
                    latestCellularNetOff = getEventTimestamp(cellularNetOff, latestNyom?.event?.latestCellularNetOff),
                    locationCollectionStarted = locationCollectionStarted,
                    latestLocationCollectionStarted = getEventTimestamp(locationCollectionStarted, latestNyom?.event?.latestLocationCollectionStarted),
                    locationCollectionStopped = locationCollectionStopped,
                    latestLocationCollectionStopped = getEventTimestamp(locationCollectionStopped, latestNyom?.event?.latestLocationCollectionStopped),
                    waypoint = wayPoint,
                    latestWaypoint = getEventTimestamp(wayPoint, latestNyom?.event?.latestWaypoint),
                    tripStart = tripStart,
                    latestTripStart = getEventTimestamp(tripStart, latestNyom?.event?.latestTripStart),
                    tripStop = tripStop,
                    latestTripStop = getEventTimestamp(tripStop, latestNyom?.event?.latestTripStop),
                    turnOnValue = turnOnValue,
                    latestTurnOnValue = getEventTimestamp(turnOnValue, latestNyom?.event?.latestTurnOnValue)
            )
        }

        private fun getEvent(getFieldFun: (EventsOuterClass.Events) -> Boolean): Boolean {
            val numberOfEvents = batchOfProcesableEntries
                    .filter { it.entryCase == IncomingNyomEntryOuterClass.IncomingNyomEntry.EntryCase.EVENTS }
                    .count { getFieldFun(it.events) }
            return numberOfEvents.let {
                if (numberOfEvents > 1) {
                    numberOfSkippedEvents += numberOfEvents - 1
                }
                numberOfEvents > 0
            }
        }

        private fun getEventTimestamp(newEventValue: Boolean, latestTimestamp: Long?): Long {
            return when {
                newEventValue -> timestamp
                latestTimestamp !== null -> latestTimestamp
                else -> -1L
            }
        }

        private fun getEvent(getFieldFun: (EventsOuterClass.Events) -> Int): Int {
            val events = batchOfProcesableEntries
                    .filter { it.entryCase == IncomingNyomEntryOuterClass.IncomingNyomEntry.EntryCase.EVENTS }
                    .filter { getFieldFun(it.events) > 0 }
            val lastWithPresent = events.lastOrNull()?.events

            if (events.size > 1) {
                numberOfSkippedEvents += events.size - 1
            }

            return when {
                lastWithPresent != null -> getFieldFun(lastWithPresent)
                else -> 0
            }
        }

        private fun getEventTimestamp(newEventValue: Int, latestTimestamp: Long?): Long {
            return when {
                newEventValue > 0 -> timestamp
                latestTimestamp !== null -> latestTimestamp
                else -> -1L
            }
        }

        private fun newStatus(): Status {
            return Status(
                    deviceInfo = getStatus(StatusOuterClass.Status::getDeviceInfo, latestNyom?.status?.deviceInfo, ""),
                    batteryVoltageMv = getStatus(StatusOuterClass.Status::getBatteryVoltage, latestNyom?.status?.batteryVoltageMv, 0),
                    batteryPercent = getStatus(StatusOuterClass.Status::getBatteryPercent, latestNyom?.status?.batteryPercent, 0),
                    gpsSatNumInFix = getStatus(StatusOuterClass.Status::getGpsSatNumInFix, latestNyom?.status?.gpsSatNumInFix, 0),
                    gpsAllSatNum = getStatus(StatusOuterClass.Status::getGpsAllSatNum, latestNyom?.status?.gpsAllSatNum, 0),
                    accuracyStats = getStatus(StatusOuterClass.Status::getAccuracyStats, latestNyom?.status?.accuracyStats, ""),
                    acclrmtrStats = getStatus(StatusOuterClass.Status::getAcclrmtrStats, latestNyom?.status?.acclrmtrStats, "")
            )
        }

        private fun <T> getStatus(getFieldFun: (StatusOuterClass.Status) -> T, latest: T?, default: T): T {
            val statuses = batchOfProcesableEntries
                    .filter { it.entryCase == IncomingNyomEntryOuterClass.IncomingNyomEntry.EntryCase.STATUS }
                    .filter { present(getFieldFun(it.status)) }
            val lastWithPresent = statuses.lastOrNull()?.status

            return when {
                lastWithPresent != null -> {
                    if (statuses.size > 1) {
                        numberOfSkippedStatuses += statuses.size - 1
                    }
                    getFieldFun(lastWithPresent)
                }
                latestNyom != null -> latest!!
                else -> default
            }
        }

        private fun <T> present(value: T): Boolean {
            return (value is String && value.isNotBlank()
                    || value is Int && value > 0)
        }

        fun getResult() = Result(newNyom, numberOfSkippedLocations, numberOfSkippedEvents, numberOfSkippedStatuses)
    }

    data class Result(val nyom: Nyom, val numberOfSkippedLocations: Int, val numberOfSkippedEvents: Int,
                      val numberOfSkippedStatuses: Int)
}

