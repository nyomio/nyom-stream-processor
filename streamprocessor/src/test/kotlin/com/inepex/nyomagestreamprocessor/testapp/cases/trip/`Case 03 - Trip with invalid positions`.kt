package com.inepex.nyomagestreamprocessor.testapp.cases.trip

import com.inepex.nyomagestreamprocessor.api.incomingnyom.LocationOuterClass.Location
import com.inepex.nyomagestreamprocessor.api.incomingnyom.IncomingNyomEntryOuterClass
import com.inepex.nyomagestreamprocessor.schema.elastic.*
import com.inepex.nyomagestreamprocessor.schema.elastic.nyom.*
import com.inepex.nyomagestreamprocessor.schema.elastic.trip.Trip
import com.inepex.nyomagestreamprocessor.testapp.cases.latE5
import com.inepex.nyomagestreamprocessor.testapp.cases.lonE5
import com.inepex.nyomagestreamprocessor.testapp.common.incomingnyomstreambuilder.IncomingNyomStreamBuilder

class `Case 03 - Trip with invalid positions`: TripCase() {

    override val entriesForFirstWindow =
            IncomingNyomStreamBuilder("1")
                    .location(Location.newBuilder().setTimestamp(0L).latE5(0.0).lonE5(0.0).build()) //0
                    // must start with a valid movement
                    .movement(30, lonDiffMeter = 100) //1
                    .movement(30, lonDiffMeter = 100) //2
                    // extend the movement a little bit
                    .movement(30, lonDiffMeter = 100) //3
                    .movement(30, lonDiffMeter = 100) //4
                    // invalid positions
                    .movement(1, lonDiffMeter = 10000) //5
                    .movement(1, lonDiffMeter = 10) //6
                    .movement(1, lonDiffMeter = 10) //7
                    // wait
                    .movement(120, lonDiffMeter = -10020) //8
                    // next valid movement
                    .movement(30, lonDiffMeter = 100) //9
                    .movement(30, lonDiffMeter = 100) //10
                    .movement(30, lonDiffMeter = 100) //11
                    .getStream()

    override val expectedNumberOfTripInserts = 1

    override val expectedTrips = listOf(
            Trip(1L, GeoPoint(0.0, 0.0), GeoPoint(0.0, 0.00353),
                    0L, 120000L, 123000,
                    "LINESTRING (0.0 0.0, 8.9E-4 0.0, 0.00178 0.0, 0.00267 0.0, 0.00356 0.0, 0.00353 0.0)",
                    emptyList(), Mapping("", "", ""))
    )

}
