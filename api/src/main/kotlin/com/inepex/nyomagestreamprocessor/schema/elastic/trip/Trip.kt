package com.inepex.nyomagestreamprocessor.schema.elastic.trip

import com.inepex.nyomagestreamprocessor.schema.elastic.GeoPoint
import com.inepex.nyomagestreamprocessor.schema.elastic.Mapping


data class Trip(val deviceId: Long, val startPoint: GeoPoint = GeoPoint(), val stopPoint: GeoPoint = GeoPoint(),
                val startTimestamp: Long = 0L, val stopTimestamp: Long = 0L, val waitTimeMillis: Long,
                val route: String = "", val routePointTimestamps: List<Long> = emptyList(),
                val mapping: Mapping = Mapping())
