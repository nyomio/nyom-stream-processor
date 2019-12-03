package com.inepex.nyomagestreamprocessor.schemaupdater

import com.google.inject.Inject
import com.google.inject.Singleton
import com.inepex.nyomagestreamprocessor.config.Configuration
import com.inepex.nyomagestreamprocessor.elastic.ClientFactory
import com.inepex.nyomagestreamprocessor.logger.Logger
import com.inepex.nyomagestreamprocessor.schemaupdater.SchemaVersion.current
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.action.update.UpdateRequest
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.indices.CreateIndexRequest
import org.elasticsearch.client.indices.GetIndexRequest
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.builder.SearchSourceBuilder


@Singleton
class SchemaUpdaterService
@Inject constructor(private val logger: Logger, private val config: Configuration,
                    private val completeSchemaCreatorService: CompleteSchemaCreatorService,
                    private val elasticClientFactory: ClientFactory) {

    private var updateMap = sortedMapOf<Int, UpdateBase>()

    fun registerUpdate(version: Int, update: UpdateBase) {
        updateMap[version] = update
    }

    fun execute() {
        elasticClientFactory.get().use { client ->
            val indexExistsResponse = client.indices().exists(GetIndexRequest(SchemaVersion.index), RequestOptions.DEFAULT)
            if (!indexExistsResponse) {
                client.indices().create(CreateIndexRequest(SchemaVersion.index), RequestOptions.DEFAULT)
            }

            val searchResponse = client.search(SearchRequest(SchemaVersion.index).apply {
                source(SearchSourceBuilder().apply {
                    query(QueryBuilders.matchAllQuery())
                })
            }, RequestOptions.DEFAULT)
            if (searchResponse.hits.totalHits.value == 0L) {
                logger.info("New deploy was detected.")
                completeSchemaCreatorService.execute()

                client.index(IndexRequest(SchemaVersion.index).apply {
                    id("1")
                    source(mapOf(current to 0))
                }, RequestOptions.DEFAULT)

            } else {
                val schemaVersionDocument = searchResponse.hits.getAt(0).sourceAsMap
                val deploymentVersion = schemaVersionDocument[current] as Int
                val latestVersion = updateMap.lastKey()
                logger.info("Deployment version was $deploymentVersion while " +
                        "latest version was $latestVersion")
                when {
                    deploymentVersion < latestVersion -> {
                        executeUpdates(deploymentVersion)

                        client.update(UpdateRequest(SchemaVersion.index, "1").apply {
                            doc(mapOf(current to latestVersion))
                        }, RequestOptions.DEFAULT)

                        logger.info("Schema version was updated to $latestVersion")

                    }
                    deploymentVersion > latestVersion -> {/* TODO revert schema */ }
                    else -> logger.info("Schema was OK")
                }
            }

        }
    }

    private fun executeUpdates(deploymentVersion: Int) {
        updateMap.tailMap(deploymentVersion + 1).forEach { (_, update) ->
            update.update()
        }
    }
}
