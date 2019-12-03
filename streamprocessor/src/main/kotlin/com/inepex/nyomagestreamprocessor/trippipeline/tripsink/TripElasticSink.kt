package com.inepex.nyomagestreamprocessor.trippipeline.tripsink

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.inepex.nyomagestreamprocessor.pipeline.Dependencies
import com.inepex.nyomagestreamprocessor.pipeline.ElasticSinkConfigService
import com.inepex.nyomagestreamprocessor.pipeline.PipelineLifecycleHooks
import com.inepex.nyomagestreamprocessor.schema.elastic.GeoPoint
import com.inepex.nyomagestreamprocessor.schema.elastic.trip.Trip
import com.inepex.nyomagestreamprocessor.trippipeline.Schema
import com.inepex.nyomagestreamprocessor.trippipeline.context.Step2_MapToTrip
import org.apache.flink.streaming.connectors.elasticsearch.ElasticsearchSinkFunction
import org.apache.flink.streaming.connectors.elasticsearch6.ElasticsearchSink
import org.apache.http.HttpHost
import org.elasticsearch.client.Requests
import com.fasterxml.jackson.module.kotlin.*

class TripElasticSinkFactory {

    fun get(): ElasticsearchSink<Step2_MapToTrip> {
        val config = Dependencies.get().config
        return ElasticsearchSink.Builder<Step2_MapToTrip>(listOf(HttpHost(config.elasticHost, config.elasticPort,
                "https")), ElasticsearchSinkFunction<Step2_MapToTrip> { context, ctx, indexer ->
            Dependencies.get().logger.debug("trip sent to elastic")
            indexer.add(Requests.indexRequest()
                    .index(Schema.TRIP_ELASTIC_INDEX)
//                    .id(context.resultNyom.timestampMillis.toString())
                    .type("_doc")
                    .source(jacksonObjectMapper()
                            .writeValueAsString(context.trip), Requests.INDEX_CONTENT_TYPE))
            PipelineLifecycleHooks.tripElasticSinkInsert?.invoke()
        }).let(ElasticSinkConfigService.Companion::configure)
    }

}

