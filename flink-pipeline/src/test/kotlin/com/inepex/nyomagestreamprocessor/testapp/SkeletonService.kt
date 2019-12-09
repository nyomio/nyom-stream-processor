package com.inepex.nyomagestreamprocessor.testapp

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.Inject
import com.inepex.nyomagestreamprocessor.elastic.ClientFactory
import com.inepex.nyomagestreamprocessor.nyompipeline.Schema as NyomSchema
import com.inepex.nyomagestreamprocessor.trippipeline.Schema as TripSchema
import com.inepex.nyomagestreamprocessor.testapp.cases.Case
import com.inepex.nyomagestreamprocessor.testapp.common.NewSchemaService
import com.inepex.nyomagestreamprocessor.testapp.common.NyomPipelineExecutorService
import com.inepex.nyomagestreamprocessor.testapp.common.SleepWithCauseService
import com.inepex.nyomagestreamprocessor.testapp.common.kafkaproducer.Client
import io.reactivex.Single
import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.junit.Assert

class SkeletonService
@Inject constructor(
        private val sleepWithCauseService: SleepWithCauseService,
        private val client: Client,
        private val newSchemaService: NewSchemaService,
        private val nyomPipelineExecutorService: NyomPipelineExecutorService,
        private val clientFactory: ClientFactory
) {

    private lateinit var case: Case

    fun execute(case: Case) {
        this.case = case
        Single.just(1)
                .map {
                    newSchemaService.execute()
                }.flatMap {
                    nyomPipelineExecutorService.execute()
                }.map { client.send(case.entriesForFirstWindow) }
                .map { sleepWithCauseService.execute(2000, "Next flink processing window") }
                .map { client.send(case.entriesForSecondWindow) }
                .flatMap {
                    if (case.expectedNumberOfNyomInserts > -1) {
                        nyomPipelineExecutorService.waitNyomInserts(case.expectedNumberOfNyomInserts)
                    } else {
                        Single.just(true)
                    }
                }
                .map { sleepWithCauseService.execute(1000, "Elastic document writes") }
                .map {
                    clientFactory.get().use { client ->
                        val searchResponse = client.search(SearchRequest(NyomSchema.NYOM_ELASTIC_INDEX).apply {
                            source(SearchSourceBuilder().apply {
                                query(QueryBuilders.matchAllQuery())
                            })
                        }, RequestOptions.DEFAULT)

                        if (case.expectedNumberOfNyomInserts > -1) {
                            assertNyoms(searchResponse)
                        }
                    }
                }
                .flatMap {
                    if (case.expectedNumberOfTripInserts > -1) {
                        nyomPipelineExecutorService.waitTripInserts(case.expectedNumberOfTripInserts)
                    } else {
                        Single.just(true)
                    }
                }
                .map { sleepWithCauseService.execute(2000, "Elastic document writes") }
                .map {
                    clientFactory.get().use { client ->
                        val searchResponse = client.search(SearchRequest(TripSchema.TRIP_ELASTIC_INDEX).apply {
                            source(SearchSourceBuilder().apply {
                                query(QueryBuilders.matchAllQuery())
                            })
                        }, RequestOptions.DEFAULT)

                        if (case.expectedNumberOfTripInserts > -1) {
                            assertTrips(searchResponse)
                        }
                    }
                }
                .map { nyomPipelineExecutorService.shutdown() }
                .map { sleepWithCauseService.execute(1000, "Flink shutdown") }
                .blockingGet()
    }

    private fun assertNyoms(searchResponse: SearchResponse) {
        Assert.assertEquals(case.caseName(), case.expectedNyoms.size, searchResponse.hits.totalHits.value.toInt())
        case.expectedNyoms.forEachIndexed { index, nyom ->
            Assert.assertEquals("${case.caseName()} - nyom nr $index:",
                    ObjectMapper().writeValueAsString(nyom),
                    searchResponse.hits.getAt(index).sourceAsString)

        }
    }

    private fun assertTrips(searchResponse: SearchResponse) {
        Assert.assertEquals(case.caseName(), case.expectedTrips.size, searchResponse.hits.totalHits.value.toInt())
        case.expectedTrips.forEachIndexed { index, trip ->
            Assert.assertEquals("${case.caseName()} - trip nr $index:",
                    ObjectMapper().writeValueAsString(trip),
                    searchResponse.hits.getAt(index).sourceAsString)

        }
    }

}
