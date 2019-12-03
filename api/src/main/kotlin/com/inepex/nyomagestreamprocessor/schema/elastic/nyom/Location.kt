package com.inepex.nyomagestreamprocessor.schema.elastic.nyom

import com.inepex.nyomagestreamprocessor.schema.elastic.GeoPoint

data class Location(val coordinates: GeoPoint = GeoPoint(), val altitudeMeters: Int = 0, val headingDegrees: Int = 0,
                    val speedDMPS: Int = 0, val accuracyMeter: Int = 0, val interpolated: Boolean = true)

