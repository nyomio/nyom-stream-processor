package com.inepex.nyomagestreamprocessor.testapp.cases

import com.inepex.nyomagestreamprocessor.api.incomingnyom.IncomingNyomEntryOuterClass.IncomingNyomEntry
import com.inepex.nyomagestreamprocessor.api.incomingnyom.LocationOuterClass.Location
import com.inepex.nyomagestreamprocessor.api.incomingnyom.IncomingNyomEntryOuterClass
import com.inepex.nyomagestreamprocessor.schema.elastic.nyom.Nyom
import com.inepex.nyomagestreamprocessor.schema.elastic.trip.Trip

interface Case {
    val entriesForFirstWindow: List<IncomingNyomEntry>
    val entriesForSecondWindow: List<IncomingNyomEntry>
    val expectedNumberOfNyomInserts: Int
    val expectedNyoms: List<Nyom>
    val expectedNumberOfTripInserts: Int
    val expectedTrips: List<Trip>

    fun caseName() = this::class.simpleName

    fun defaultNatIdNyomBuilder() = IncomingNyomEntryOuterClass.IncomingNyomEntry.newBuilder().apply {
        nativeId = "1"
    }

    fun nyomWithDefaultNatId(timestamp: Long, lat: Double, lon: Double) =
            defaultNatIdNyomBuilder().apply {
                location = Location.newBuilder().apply {
                    setTimestamp(timestamp)
                    latE5(lat)
                    lonE5(lon)
                }.build()
            }.build()
}
