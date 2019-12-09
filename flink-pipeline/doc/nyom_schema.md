# Nyom schema (? = optional)
 
deviceId: String
objectId1: String?
objectId2: String?
objectId3: String?
userId: Long
companyId: Long?
timestampMillis: Long
lat: Double
lng: Double
loc.altitudeMeters: Double?
loc.headingDegrees: Double?
loc.speedDMPS: Double? - speed in decimeters per second
loc.satNumInFix: Short?
loc.allSatNum: Short?
loc.accuracyMeters: Float?
network.mcc: Short? - mobile country code
network.mnc: Short? - mobile network code
network.lac: Int? ?
network.cellId: Int?
network.signalStrenghtPercent: Float?
device.batteryMilliVolts: Int?
device.batteryPercent: Short?
device.externalVoltageMilliVolts: Int?

# Prefixes for undefined properties
Prefixes are used to set proper property type in ElasticSearch.

String: s_
Number: l_
Fraction: d_
Boolean: b_

For example: l_device.digIn1, s_device.deviceInfo
