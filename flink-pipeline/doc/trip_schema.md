# Trip schema (? = optional)

deviceId: String
objectId1: String?
objectId2: String?
objectId3: String?
startTimeMillis: Long
stopTimeMillis: Long?
timespentInWayPointMillis: Long?
startLat: Double
startLng: Double
stopLat: Double?
stopLng: Double?
distanceKMeters: Double
maxSpeedKMH: Double
avgSpeedKMH: Double
startAddress: String
stopAddress: String?
startAddressIds: String
stopAddressIds: String
routePolygon: String
manual: Boolan
replacedByTripId: Long?
replacement: Boolean
//TODO: amendment
//TODO: POIs with name and any additional info
