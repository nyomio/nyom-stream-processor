package com.inepex.nyomagestreamprocessor.testapp

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Inject
import com.google.inject.Singleton
import com.inepex.nyomagestreamprocessor.api.incomingnyom.IncomingNyomEntryOuterClass
import com.inepex.nyomagestreamprocessor.api.incomingnyom.LocationOuterClass
import com.inepex.nyomagestreamprocessor.config.Configuration
import com.inepex.nyomagestreamprocessor.config.ConfigurationBuilderService
import com.inepex.nyomagestreamprocessor.logger.Logger
import com.inepex.nyomagestreamprocessor.nyompipeline.mapincomingnyom.Constants
import com.inepex.nyomagestreamprocessor.testapp.common.kafkaproducer.Client

fun main() {

    val injector = Guice.createInjector(GuiceModule())
    injector.getInstance(App::class.java).execute()
}

class GuiceModule constructor() : AbstractModule() {

    override fun configure() {
        bind(Configuration::class.java).toInstance(ConfigurationBuilderService.build(false, "tiborsomodi"))
        bind(Logger::class.java).toInstance(Logger())
    }
}

@Singleton
class App @Inject constructor(private val kafkaClient: Client) {

    val now = System.currentTimeMillis()

    val entries = listOf<IncomingNyomEntryOuterClass.IncomingNyomEntry>(
            IncomingNyomEntryOuterClass.IncomingNyomEntry.newBuilder().apply {
                nativeId = "1"
                location = LocationOuterClass.Location.newBuilder().apply {
                    timestamp = now
                    lat = 1 * Constants.E5.toInt()
                    lon = 1 * Constants.E5.toInt()
                }.build()
            }.build(),
            IncomingNyomEntryOuterClass.IncomingNyomEntry.newBuilder().apply {
                nativeId = "1"
                location = LocationOuterClass.Location.newBuilder().apply {
                    timestamp = now + 1000L
                    lat = 2 * Constants.E5.toInt()
                    lon = 2 * Constants.E5.toInt()
                }.build()
            }.build(),
            IncomingNyomEntryOuterClass.IncomingNyomEntry.newBuilder().apply {
                nativeId = "1"
                location = LocationOuterClass.Location.newBuilder().apply {
                    timestamp = now + 1200L
                    lat = 3 * Constants.E5.toInt()
                    lon = 3 * Constants.E5.toInt()
                }.build()
            }.build()
    )

    fun execute() {
        kafkaClient.send(entries)

    }
}


