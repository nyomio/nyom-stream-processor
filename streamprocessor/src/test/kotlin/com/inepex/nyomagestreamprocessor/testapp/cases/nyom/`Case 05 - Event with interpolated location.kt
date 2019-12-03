package com.inepex.nyomagestreamprocessor.testapp.cases.nyom

import com.inepex.nyomagestreamprocessor.api.incomingnyom.EventsOuterClass.*
import com.inepex.nyomagestreamprocessor.api.incomingnyom.IncomingNyomEntryOuterClass.IncomingNyomEntry
import com.inepex.nyomagestreamprocessor.api.incomingnyom.LocationOuterClass.Location
import com.inepex.nyomagestreamprocessor.api.incomingnyom.IncomingNyomEntryOuterClass
import com.inepex.nyomagestreamprocessor.schema.elastic.*
import com.inepex.nyomagestreamprocessor.schema.elastic.nyom.*
import com.inepex.nyomagestreamprocessor.schema.elastic.nyom.Status as NyomStatus
import com.inepex.nyomagestreamprocessor.nyompipeline.mapincomingnyom.Constants
import com.inepex.nyomagestreamprocessor.schema.elastic.trip.Trip
import com.inepex.nyomagestreamprocessor.testapp.cases.Case

class `Case 05 - Event with interpolated location`: NyomCase() {

    override val entriesForFirstWindow = listOf<IncomingNyomEntryOuterClass.IncomingNyomEntry>(
            IncomingNyomEntryOuterClass.IncomingNyomEntry.newBuilder().apply {
                nativeId = "1"
                location = Location.newBuilder().apply {
                    timestamp = 0L
                    lat = 0 * Constants.E5.toInt()
                    lon = 0 * Constants.E5.toInt()
                }.build()
            }.build(),
            IncomingNyomEntry.newBuilder().apply {
                nativeId = "1"
                events = Events.newBuilder().apply {
                    timestamp = 500L
                    sos = true

                }.build()
            }.build(),
            IncomingNyomEntry.newBuilder().apply {
                nativeId = "1"
                location = Location.newBuilder().apply {
                    timestamp = 1000L
                    lat = 10 * Constants.E5.toInt()
                    lon = 10 * Constants.E5.toInt()
                }.build()
            }.build()
    )

    override val entriesForSecondWindow = listOf<IncomingNyomEntry>(
    )

    override val expectedNumberOfNyomInserts = 4

    override val expectedNyoms = listOf(
            Nyom(1L, 0L,
                    Location(coordinates = GeoPoint(0.0, 0.0), interpolated = false),
                    Event(),
                    NyomStatus(),
                    Mapping()),
            Nyom(1L, 500L,
                    Location(coordinates = GeoPoint(5.0, 5.0), interpolated = true),
                    Event(sos = true, latestSosTimestamp = 500L),
                    NyomStatus(),
                    Mapping()),
            Nyom(1L, 1000L,
                    Location(coordinates = GeoPoint(10.0, 10.0), interpolated = false),
                    Event(latestSosTimestamp = 500L),
                    NyomStatus(),
                    Mapping())
    )

}
