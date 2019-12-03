package com.inepex.nyomagestreamprocessor.nyompipeline.context

import com.inepex.nyomagestreamprocessor.api.incomingnyom.IncomingNyomEntryOuterClass
import com.inepex.nyomagestreamprocessor.pipeline.HasTraceId

data class Step1_NyomKafkaSource(
        override val traceId: String,
        val incomingNyomEntry: IncomingNyomEntryOuterClass.IncomingNyomEntry
) : HasTraceId
