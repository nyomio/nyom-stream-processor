package com.inepex.nyomagestreamprocessor.testapp.cases.nyom

import com.inepex.nyomagestreamprocessor.api.incomingnyom.IncomingNyomEntryOuterClass.IncomingNyomEntry
import com.inepex.nyomagestreamprocessor.api.incomingnyom.LocationOuterClass.Location
import com.inepex.nyomagestreamprocessor.api.incomingnyom.IncomingNyomEntryOuterClass
import com.inepex.nyomagestreamprocessor.schema.elastic.*
import com.inepex.nyomagestreamprocessor.schema.elastic.nyom.*
import com.inepex.nyomagestreamprocessor.nyompipeline.mapincomingnyom.Constants
import com.inepex.nyomagestreamprocessor.schema.elastic.trip.Trip
import com.inepex.nyomagestreamprocessor.testapp.cases.Case

class `Case 01 - Locations, shouldn't be grouped`: NyomCase() {

    override val entriesForFirstWindow = listOf<IncomingNyomEntryOuterClass.IncomingNyomEntry>(
            IncomingNyomEntryOuterClass.IncomingNyomEntry.newBuilder().apply {
                nativeId = "1"
                location = Location.newBuilder().apply {
                    timestamp = 1L
                    lat = 1 * Constants.E5.toInt()
                    lon = 1 * Constants.E5.toInt()
                }.build()
            }.build(),
            IncomingNyomEntryOuterClass.IncomingNyomEntry.newBuilder().apply {
                nativeId = "1"
                location = Location.newBuilder().apply {
                    timestamp = 1000L
                    lat = 2 * Constants.E5.toInt()
                    lon = 2 * Constants.E5.toInt()
                }.build()
            }.build()

    )

    override val entriesForSecondWindow = listOf<IncomingNyomEntryOuterClass.IncomingNyomEntry>(
            IncomingNyomEntry.newBuilder().apply {
                nativeId = "1"
                location = Location.newBuilder().apply {
                    timestamp = 1200L
                    lat = 3 * Constants.E5.toInt()
                    lon = 3 * Constants.E5.toInt()
                }.build()
            }.build()

    )

    override val expectedNumberOfNyomInserts = 3

    override val expectedNyoms = listOf(
            Nyom(1L, 1L,
                    Location(GeoPoint(1.0, 1.0), 0, 0, 0, 0, false),
                    Event(),
                    Status(),
                    Mapping()),
            Nyom(1L, 1000L,
                    Location(GeoPoint(2.0, 2.0), 0, 0, 0, 0, false),
                    Event(),
                    Status(),
                    Mapping()),
            Nyom(1L, 1200L,
                    Location(GeoPoint(3.0, 3.0), 0, 0, 0, 0, false),
                    Event(),
                    Status(),
                    Mapping())
    )

}
