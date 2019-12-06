package com.inepex.nyomio

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Inject
import com.inepex.nyomio.config.Configuration
import com.inepex.nyomio.config.ConfigurationBuilderService
import com.inepex.nyomio.logger.Logger
import org.slf4j.LoggerFactory

fun main(args: Array<String>) {
    System.setProperty("APP_ID", CompanyApiTestImplApp::class.simpleName)
    LoggerFactory.getLogger("test").info("Started")
    Guice.createInjector(CompanyApiTestImplAppGuiceModule()).apply {
        getInstance(CompanyApiTestImplApp::class.java).execute()
    }

}

class CompanyApiTestImplApp @Inject constructor(private var apiServer: ApiServer) {

    fun execute() {
        apiServer.start()
    }
}

class CompanyApiTestImplAppGuiceModule constructor(): AbstractModule() {

    override fun configure() {
        bind(Configuration::class.java).toInstance(ConfigurationBuilderService.build(null))
        bind(Logger::class.java).toInstance(Logger())
    }
}

