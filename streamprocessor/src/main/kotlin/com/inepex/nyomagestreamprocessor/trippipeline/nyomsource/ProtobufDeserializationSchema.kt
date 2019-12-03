package com.inepex.nyomagestreamprocessor.trippipeline.nyomsource

import com.inepex.nyomagestreamprocessor.api.generatednyom.NyomOuterClass
import com.inepex.nyomagestreamprocessor.pipeline.Constants
import com.inepex.nyomagestreamprocessor.pipeline.Dependencies
import com.inepex.nyomagestreamprocessor.pipeline.GenerateTraceIdService
import com.inepex.nyomagestreamprocessor.trippipeline.context.Step1_NyomKafkaSource
import org.apache.flink.api.common.serialization.AbstractDeserializationSchema
import org.slf4j.MDC

class ProtoBufDeserializationSchema : AbstractDeserializationSchema<Step1_NyomKafkaSource>() {

    override fun deserialize(message: ByteArray): Step1_NyomKafkaSource {
        return Step1_NyomKafkaSource(GenerateTraceIdService().execute(),
                NyomOuterClass.Nyom.parseFrom(message)).apply {
            MDC.put(Constants.MDC_PARAM_TRACEID, this.traceId)
            Dependencies.get().logger.info("New NyomProcessing created: ${this}")
        }
    }

}
