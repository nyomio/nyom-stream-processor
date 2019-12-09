package com.inepex.nyomagestreamprocessor.nyompipeline.nyomsink

import com.inepex.nyomagestreamprocessor.pipeline.Dependencies
import com.inepex.nyomagestreamprocessor.nyompipeline.context.Step4_MapIncomingNyom
import com.inepex.nyomagestreamprocessor.trippipeline.Schema
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer
import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import java.util.*

class NyomKafkaSink : FlinkKafkaProducer<Step4_MapIncomingNyom>(Schema.GENERATED_NYOM_TOPIC,
        KafkaSerializationSchema<Step4_MapIncomingNyom> { element, timestamp ->
            ProducerRecord(Schema.GENERATED_NYOM_TOPIC, "partition".toByteArray(),
                    NyomMapper.INSTANCE.toProto(element.resultNyom).toByteArray())
        },
        Properties().apply {
            setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Dependencies.get().config.kafkaAddress)
            setProperty(ProducerConfig.ACKS_CONFIG, "all") // wait for all replicas to ack
            setProperty(ProducerConfig.LINGER_MS_CONFIG, "100") // send delay (ms)
            setProperty(ProducerConfig.BATCH_SIZE_CONFIG, "16384") // batch size (Kb)
            setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true") // exactly once delivery
            setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5") // max nr of unacknowledged requests
        }, Semantic.EXACTLY_ONCE)
