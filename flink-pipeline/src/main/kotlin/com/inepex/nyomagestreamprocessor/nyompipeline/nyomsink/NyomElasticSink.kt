package com.inepex.nyomagestreamprocessor.nyompipeline.nyomsink

import com.fasterxml.jackson.databind.ObjectMapper
import com.inepex.nyomagestreamprocessor.nyompipeline.Schema
import com.inepex.nyomagestreamprocessor.pipeline.Dependencies
import com.inepex.nyomagestreamprocessor.pipeline.PipelineLifecycleHooks
import com.inepex.nyomagestreamprocessor.nyompipeline.context.Step4_MapIncomingNyom
import com.inepex.nyomagestreamprocessor.pipeline.ElasticSinkConfigService
import org.apache.flink.streaming.connectors.elasticsearch.ElasticsearchSinkFunction
import org.apache.flink.streaming.connectors.elasticsearch6.ElasticsearchSink
import org.apache.http.HttpHost
import org.elasticsearch.client.Requests

class NyomElasticSinkFactory {

    fun get(): ElasticsearchSink<Step4_MapIncomingNyom> {
        val config = Dependencies.get().config
        return ElasticsearchSink.Builder<Step4_MapIncomingNyom>(listOf(HttpHost(config.elasticHost, config.elasticPort,
                "https")), ElasticsearchSinkFunction<Step4_MapIncomingNyom> { context, ctx, indexer ->
            Dependencies.get().logger.debug("sent to elastic")
            indexer.add(Requests.indexRequest()
                    .index(Schema.NYOM_ELASTIC_INDEX)
                    .id(context.resultNyom.timestampMillis.toString())
                    .type("_doc")
                    .source(ObjectMapper().writeValueAsString(context.resultNyom), Requests.INDEX_CONTENT_TYPE))
            PipelineLifecycleHooks.nyomElasticSinkInsert?.invoke()
        }).let(ElasticSinkConfigService.Companion::configure)
    }

}
