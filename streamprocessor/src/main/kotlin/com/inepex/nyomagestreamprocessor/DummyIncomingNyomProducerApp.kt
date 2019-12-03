package com.inepex.nyomagestreamprocessor

import ch.qos.logback.classic.LoggerContext
import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Inject
import com.inepex.nyomagestreamprocessor.api.incomingnyom.LocationOuterClass
import com.inepex.nyomagestreamprocessor.api.incomingnyom.IncomingNyomEntryOuterClass
import com.inepex.nyomagestreamprocessor.config.Configuration
import com.inepex.nyomagestreamprocessor.config.ConfigurationBuilderService
import com.inepex.nyomagestreamprocessor.logger.Logger
import com.inepex.nyomagestreamprocessor.nyompipeline.Schema
import com.inepex.nyomagestreamprocessor.nyompipeline.mapincomingnyom.Constants
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


fun main() {
    System.setProperty("APP_ID", DummyIncomingNyomProducerApp::class.simpleName)
    LoggerFactory.getLogger("test").info("Started")
    Guice.createInjector(DummyIncomingNyomProducerAppGuiceModule()).apply {
        getInstance(DummyIncomingNyomProducerApp::class.java).execute()
    }
}

class DummyIncomingNyomProducerApp
@Inject constructor(private val configuration: Configuration){

    fun execute() {
        val executor = Executors.newScheduledThreadPool(1)

        val producer = KafkaProducer<String, ByteArray>(Properties().apply {
            put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, configuration.kafkaAddress)
            put(ProducerConfig.ACKS_CONFIG, "all")
            put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
            put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArraySerializer")
        })

        val deviceIds = arrayOf(1L, 2L, 3L)
        val random = Random()

        executor.scheduleAtFixedRate({
            val key = deviceIds.get(random.nextInt(deviceIds.size))
            val lat = random.nextDouble() * 90
            val lon = random.nextDouble() * 90
            val data = IncomingNyomEntryOuterClass.IncomingNyomEntry.newBuilder().apply {
                nativeId = key.toString()
                location = LocationOuterClass.Location.newBuilder().apply {
                    timestamp = System.currentTimeMillis()
                    setLat((lat * Constants.E5).toInt())
                    setLon((lon * Constants.E5).toInt())
                    altitudeMeters = 0
                    headingDegrees = 0
                    speedDMPS = 1
                    accuracyMeter = 0
                }.build()

            }.build()


            producer.send(ProducerRecord(Schema.INCOMING_NYOM_TOPIC, "partition", data.toByteArray()))
        }, 0L, 30000, TimeUnit.MILLISECONDS)

        Runtime.getRuntime().addShutdownHook(Thread {
            val lc = LoggerFactory.getILoggerFactory() as LoggerContext
            lc.stop()
        })
    }
}

class DummyIncomingNyomProducerAppGuiceModule constructor(): AbstractModule() {

    override fun configure() {
        bind(Configuration::class.java).toInstance(ConfigurationBuilderService.build(null))
        bind(Logger::class.java).toInstance(Logger())
    }
}

