package com.inepex.nyomagestreamprocessor.trippipeline.context

import com.inepex.nyomagestreamprocessor.pipeline.HasTraceId
import com.inepex.nyomagestreamprocessor.schema.elastic.trip.Trip

data class Step2_MapToTrip(
        override val traceId: String,
        val trip: Trip
) : HasTraceId
