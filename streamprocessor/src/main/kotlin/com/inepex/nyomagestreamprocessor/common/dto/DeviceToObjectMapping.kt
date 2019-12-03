package com.inepex.nyomagestreamprocessor.common.dto

data class DeviceToObjectMapping (val deviceId: Long, val objectId: String, val beginTimestamp: Long,
                                  val endTimestamp: Long?)
