package com.inepex.nyomagestreamprocessor.testapp.cases.nyom

import com.inepex.nyomagestreamprocessor.api.incomingnyom.LocationOuterClass.Location
import com.inepex.nyomagestreamprocessor.api.incomingnyom.IncomingNyomEntryOuterClass
import com.inepex.nyomagestreamprocessor.schema.elastic.*
import com.inepex.nyomagestreamprocessor.schema.elastic.nyom.*
import com.inepex.nyomagestreamprocessor.schema.elastic.nyom.Status as NyomStatus
import com.inepex.nyomagestreamprocessor.nyompipeline.mapincomingnyom.Constants
import com.inepex.nyomagestreamprocessor.schema.elastic.trip.Trip
import com.inepex.nyomagestreamprocessor.testapp.cases.Case

class `Case 03 - Late location, should be grouped using an update` : NyomCase() {

    override val entriesForFirstWindow = listOf<IncomingNyomEntryOuterClass.IncomingNyomEntry>(
            IncomingNyomEntryOuterClass.IncomingNyomEntry.newBuilder().apply {
                nativeId = "1"
                location = Location.newBuilder().apply {
                    timestamp = 1L
                    lat = 1 * Constants.E5.toInt()
                    lon = 1 * Constants.E5.toInt()
                }.build()
            }.build()
    )

    override val entriesForSecondWindow = listOf<IncomingNyomEntryOuterClass.IncomingNyomEntry>(
            IncomingNyomEntryOuterClass.IncomingNyomEntry.newBuilder().apply {
                nativeId = "1"
                location = Location.newBuilder().apply {
                    timestamp = 2L
                    lat = 2 * Constants.E5.toInt()
                    lon = 2 * Constants.E5.toInt()
                }.build()
            }.build()
    )

    /**
     * one insert and one udpate
     */
    override val expectedNumberOfNyomInserts = 2

    override val expectedNyoms = listOf(
            Nyom(1L, 1L,
                    Location(coordinates = GeoPoint(2.0, 2.0), interpolated = false),
                    Event(),
                    NyomStatus(),
                    Mapping())
    )

}
