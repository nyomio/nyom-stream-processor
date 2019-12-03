package com.inepex.nyomagestreamprocessor.testapp.cases

import com.inepex.nyomagestreamprocessor.api.incomingnyom.LocationOuterClass
import com.inepex.nyomagestreamprocessor.nyompipeline.mapincomingnyom.Constants

fun LocationOuterClass.Location.Builder.latE5(lat: Double): LocationOuterClass.Location.Builder {
    this.lat = (lat * Constants.E5).toInt()
    return this
}

fun LocationOuterClass.Location.Builder.lonE5(lon: Double): LocationOuterClass.Location.Builder {
    this.lon = (lon * Constants.E5).toInt()
    return this
}
