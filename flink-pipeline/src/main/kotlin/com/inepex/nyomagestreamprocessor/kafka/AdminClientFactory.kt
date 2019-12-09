package com.inepex.nyomagestreamprocessor.kafka

import com.google.inject.Inject
import com.google.inject.Singleton
import com.inepex.nyomagestreamprocessor.config.Configuration
import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.AdminClientConfig

@Singleton
class AdminClientFactory @Inject constructor(private var configuration: Configuration) {

    fun get(): AdminClient {
        return AdminClient.create(mapOf(
                AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG to configuration.kafkaAddress
        ))
    }

}
