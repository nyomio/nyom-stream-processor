package com.inepex.nyomagestreamprocessor.testapp.cases.nyom

import com.inepex.nyomagestreamprocessor.api.incomingnyom.EventsOuterClass
import com.inepex.nyomagestreamprocessor.api.incomingnyom.IncomingNyomEntryOuterClass.IncomingNyomEntry
import com.inepex.nyomagestreamprocessor.api.incomingnyom.LocationOuterClass.Location
import com.inepex.nyomagestreamprocessor.api.incomingnyom.StatusOuterClass.Status
import com.inepex.nyomagestreamprocessor.api.incomingnyom.IncomingNyomEntryOuterClass
import com.inepex.nyomagestreamprocessor.schema.elastic.*
import com.inepex.nyomagestreamprocessor.schema.elastic.nyom.*
import com.inepex.nyomagestreamprocessor.schema.elastic.nyom.Status as NyomStatus
import com.inepex.nyomagestreamprocessor.nyompipeline.mapincomingnyom.Constants
import com.inepex.nyomagestreamprocessor.schema.elastic.trip.Trip
import com.inepex.nyomagestreamprocessor.testapp.cases.Case

class `Case 04 - Events and statuses, should use latest nyom` : NyomCase() {

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
                events = EventsOuterClass.Events.newBuilder().apply {
                    timestamp = 2L
                    sos = true

                }.build()
            }.build(),
            IncomingNyomEntry.newBuilder().apply {
                nativeId = "1"
                status = Status.newBuilder().apply {
                    timestamp = 2L
                    batteryPercent = 50
                }.build()
            }.build()
    )

    override val entriesForSecondWindow = listOf<IncomingNyomEntry>(
            IncomingNyomEntry.newBuilder().apply {
                nativeId = "1"
                location = Location.newBuilder().apply {
                    timestamp = 1000L
                    lat = 2 * Constants.E5.toInt()
                    lon = 2 * Constants.E5.toInt()
                }.build()
            }.build()
    )

    override val expectedNumberOfNyomInserts = 2

    override val expectedNyoms = listOf(
            Nyom(1L, 1L,
                    Location(coordinates = GeoPoint(1.0, 1.0), interpolated = false),
                    Event(true, 1L),
                    NyomStatus(batteryPercent = 50),
                    Mapping()),
            Nyom(1L, 1000L,
                    Location(coordinates = GeoPoint(2.0, 2.0), interpolated = false),
                    Event(false, 1L),
                    NyomStatus(batteryPercent = 50),
                    Mapping())
    )

}
