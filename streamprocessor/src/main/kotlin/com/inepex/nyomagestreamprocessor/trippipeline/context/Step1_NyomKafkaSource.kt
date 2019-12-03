package com.inepex.nyomagestreamprocessor.trippipeline.context

import com.inepex.nyomagestreamprocessor.api.generatednyom.NyomOuterClass
import com.inepex.nyomagestreamprocessor.pipeline.HasTraceId

data class Step1_NyomKafkaSource(
        override val traceId: String,
        val nyom: NyomOuterClass.Nyom
) : HasTraceId
