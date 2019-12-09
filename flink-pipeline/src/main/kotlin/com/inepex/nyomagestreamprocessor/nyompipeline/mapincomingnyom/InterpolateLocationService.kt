package com.inepex.nyomagestreamprocessor.nyompipeline.mapincomingnyom

import com.google.inject.Inject
import com.inepex.nyomagestreamprocessor.schema.elastic.GeoPoint
import com.inepex.nyomagestreamprocessor.schema.elastic.nyom.Nyom
import org.apache.commons.math3.analysis.interpolation.LinearInterpolator

class InterpolateLocationService
@Inject constructor() {

    fun execute(toInterpolate: Nyom, knownLocationBefore: Nyom, knownLocationAfter: Nyom): Nyom {
        return toInterpolate.let {
            val interpolatedLat = LinearInterpolator().interpolate(
                    doubleArrayOf(knownLocationBefore.timestampMillis.toDouble(),
                            knownLocationAfter.timestampMillis.toDouble()),
                    doubleArrayOf(knownLocationBefore.location.coordinates.lat,
                            knownLocationAfter.location.coordinates.lat))
                    .value(it.timestampMillis.toDouble())
            val interpolatedLon = LinearInterpolator().interpolate(
                    doubleArrayOf(knownLocationBefore.timestampMillis.toDouble(),
                            knownLocationAfter.timestampMillis.toDouble()),
                    doubleArrayOf(knownLocationBefore.location.coordinates.lon,
                            knownLocationAfter.location.coordinates.lon))
                    .value(it.timestampMillis.toDouble())
            Nyom(it.deviceId, it.timestampMillis,
                    location = it.location.copy(coordinates = GeoPoint(interpolatedLat,
                            interpolatedLon)),
                    event = it.event.copy(),
                    status = it.status.copy(),
                    mapping = it.mapping.copy())
        }

    }
}
