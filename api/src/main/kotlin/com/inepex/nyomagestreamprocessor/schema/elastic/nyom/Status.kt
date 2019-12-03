package com.inepex.nyomagestreamprocessor.schema.elastic.nyom

data class Status (val deviceInfo: String = "", val batteryVoltageMv: Int = 0, val batteryPercent: Int = 0,
                   val gpsSatNumInFix: Int = 0, val gpsAllSatNum: Int = 0, val accuracyStats: String = "",
                   val acclrmtrStats: String = "")
