package com.inepex.nyomagestreamprocessor

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Inject
import com.inepex.nyomagestreamprocessor.basicdevicemanager.ApiServer
import com.inepex.nyomagestreamprocessor.config.Configuration
import com.inepex.nyomagestreamprocessor.config.ConfigurationBuilderService
import com.inepex.nyomagestreamprocessor.logger.Logger
import org.slf4j.LoggerFactory

fun main(args: Array<String>) {
    System.setProperty("APP_ID", BasicDeviceManagerApp::class.simpleName)
    LoggerFactory.getLogger("test").info("Started")

    Guice.createInjector(BasicDeviceManagerAppGuiceModule()).apply {
        getInstance(BasicDeviceManagerApp::class.java).execute()
    }

}

class BasicDeviceManagerApp @Inject constructor(private var apiServer: ApiServer) {

    fun execute() {
        apiServer.start()
    }
}

class BasicDeviceManagerAppGuiceModule constructor(): AbstractModule() {

    override fun configure() {
        bind(Configuration::class.java).toInstance(ConfigurationBuilderService.build(null))
        bind(Logger::class.java).toInstance(Logger())
    }
}


