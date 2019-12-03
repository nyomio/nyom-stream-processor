package com.inepex.nyomagestreamprocessor.testapp.cases.trip

import com.inepex.nyomagestreamprocessor.api.incomingnyom.IncomingNyomEntryOuterClass.IncomingNyomEntry
import com.inepex.nyomagestreamprocessor.schema.elastic.nyom.Nyom
import com.inepex.nyomagestreamprocessor.schema.elastic.trip.Trip
import com.inepex.nyomagestreamprocessor.testapp.cases.Case

abstract class TripCase: Case {
    override val entriesForSecondWindow: List<IncomingNyomEntry> = emptyList()
    override val expectedNumberOfNyomInserts: Int = -1
    override val expectedNyoms: List<Nyom> = emptyList()

}
