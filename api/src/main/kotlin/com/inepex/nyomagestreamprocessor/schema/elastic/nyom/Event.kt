package com.inepex.nyomagestreamprocessor.schema.elastic.nyom

data class Event(val sos: Boolean = false, val latestSosTimestamp: Long = -1,
                 val userTurnOn: Boolean = false, val latestUserTurnOn: Long = -1,
                 val userTurnOff: Boolean = false, val latestUserTurnOff: Long = -1,
                 val sleepStateChange: Int = 0, val latestSleepStateChange: Long = -1,
                 val trafficLimitExceeded: Boolean = false, val latestTrafficLimitExceeded: Long = -1,
                 val haveNet: Boolean = false, val latestHaveNet: Long = -1,
                 val netLost: Boolean = false, val latestNetLost: Long = -1,
                 val haveGps: Boolean = false, val latestHaveGps: Long = -1,
                 val gpsLost: Boolean = false, val latestGpsLost: Long = -1,
                 val wifiOn: Boolean = false, val latestWifiOn: Long = -1,
                 val wifiOff: Boolean = false, val latestWifiOff: Long = -1,
                 val cellularNetOn: Boolean = false, val latestCellularNetOn: Long = -1,
                 val cellularNetOff: Boolean = false, val latestCellularNetOff: Long = -1,
                 val locationCollectionStarted: Boolean = false, val latestLocationCollectionStarted: Long = -1,
                 val locationCollectionStopped: Boolean = false, val latestLocationCollectionStopped: Long = -1,
                 val waypoint: Boolean = false, val latestWaypoint: Long = -1,
                 val tripStart: Boolean = false, val latestTripStart: Long = -1,
                 val tripStop: Boolean = false, val latestTripStop: Long = -1,
                 val turnOnValue: Int = 0, val latestTurnOnValue: Long = -1
                 )
