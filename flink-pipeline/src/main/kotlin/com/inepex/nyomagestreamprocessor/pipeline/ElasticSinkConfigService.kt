package com.inepex.nyomagestreamprocessor.pipeline

import org.apache.flink.streaming.connectors.elasticsearch6.ElasticsearchSink

class ElasticSinkConfigService {
    
    companion object {
        
        fun <T> configure(elasticSinkBuilder: ElasticsearchSink.Builder<T>): ElasticsearchSink<T> {
            elasticSinkBuilder.setBulkFlushMaxActions(100)
            elasticSinkBuilder.setBulkFlushInterval(500)
            elasticSinkBuilder.setRestClientFactory {
                it.setHttpClientConfigCallback {
                    Dependencies.get().elasticSetSecureConnectionService.execute(it)
                    it
                }
            }
            return elasticSinkBuilder.build()
        }
    }
}
