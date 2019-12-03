package com.inepex.nyomagestreamprocessor.elastic

import com.google.inject.Inject
import com.google.inject.Singleton
import com.inepex.nyomagestreamprocessor.config.Configuration
import org.apache.http.HttpHost
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestHighLevelClient

@Singleton
class ClientFactory @Inject constructor(private var config: Configuration,
                                        private var setSecureConnectionService: SetSecureConnectionService) {

    fun get(): RestHighLevelClient {
        return RestHighLevelClient(RestClient.builder(HttpHost(config.elasticHost, config.elasticPort, "https"))
                .setHttpClientConfigCallback {
                    setSecureConnectionService.execute(it)
                    it
                })
    }

}
