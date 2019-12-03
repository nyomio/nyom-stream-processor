package com.inepex.nyomagestreamprocessor.nyompipeline.nyomsource

import com.inepex.nyomagestreamprocessor.nyompipeline.Schema
import com.inepex.nyomagestreamprocessor.pipeline.KafkaSourcePropertiesProvider
import com.inepex.nyomagestreamprocessor.nyompipeline.context.Step1_NyomKafkaSource
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer

class NyomKafkaSourceFunction
constructor(protobufDeserializationSchema: ProtoBufDeserializationSchema,
            kafkaSourcePropertiesProvider: KafkaSourcePropertiesProvider) :
        FlinkKafkaConsumer<Step1_NyomKafkaSource>(Schema.INCOMING_NYOM_TOPIC, protobufDeserializationSchema,
                kafkaSourcePropertiesProvider.get())
