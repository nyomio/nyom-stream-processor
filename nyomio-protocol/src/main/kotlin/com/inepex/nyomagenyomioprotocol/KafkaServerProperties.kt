package com.inepex.nyomagenyomioprotocol

import com.google.inject.Inject
import com.google.inject.Singleton
import com.inepex.nyomagenyomioprotocol.config.Configuration
import org.apache.kafka.clients.producer.ProducerConfig
import java.util.*

@Singleton
class KafkaServerProperties @Inject constructor(private val config: Configuration) {

    private val properties = Properties()

    init {
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.kafkaAddress)
        properties.setProperty(ProducerConfig.ACKS_CONFIG, "all") // wait for all replicas to ack
        properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, "100") // send delay (ms)
        properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, "16384") // batch size (Kb)
        properties.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true") // exactly once delivery
        properties.setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5") // max nr of unacknowledged requests
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArraySerializer")
    }

    fun get() = properties

}
