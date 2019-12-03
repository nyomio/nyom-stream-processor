package com.inepex.nyomagestreamprocessor.pipeline

import com.inepex.nyomagestreamprocessor.config.Configuration
import java.util.*

class KafkaSourcePropertiesProvider constructor(private val config : Configuration) {

    private val properties = Properties()

    init {
        properties.setProperty("bootstrap.servers", config.kafkaAddress)
        properties.setProperty("group.id", "test")
//            setProperty("auto.offset.reset", "earliest")
    }

    fun get() = properties

}
