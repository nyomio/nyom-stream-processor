package com.inepex.nyomagestreamprocessor.trippipeline.nyomsource

import com.inepex.nyomagestreamprocessor.pipeline.KafkaSourcePropertiesProvider
import com.inepex.nyomagestreamprocessor.trippipeline.Schema
import com.inepex.nyomagestreamprocessor.trippipeline.context.Step1_NyomKafkaSource
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer

class NyomKafkaSourceFunction
constructor(protobufDeserializationSchema: ProtoBufDeserializationSchema,
            kafkaSourcePropertiesProvider: KafkaSourcePropertiesProvider) :
        FlinkKafkaConsumer<Step1_NyomKafkaSource>(Schema.GENERATED_NYOM_TOPIC, protobufDeserializationSchema,
                kafkaSourcePropertiesProvider.get())
