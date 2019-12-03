package com.inepex.nyomagestreamprocessor.testapp.cases.nyom

import com.inepex.nyomagestreamprocessor.schema.elastic.trip.Trip
import com.inepex.nyomagestreamprocessor.testapp.cases.Case

abstract class NyomCase : Case {

    override val expectedNumberOfTripInserts = -1

    override val expectedTrips = listOf<Trip>(
    )
}
