package com.inepex.nyomagestreamprocessor.schemaupdater

import com.google.inject.Inject
import com.inepex.nyomagestreamprocessor.elastic.ClientFactory
import com.inepex.nyomagestreamprocessor.kafka.AdminClientFactory
import com.inepex.nyomagestreamprocessor.logger.Logger
import com.inepex.nyomagestreamprocessor.nyompipeline.Schema as NyomSchema
import com.inepex.nyomagestreamprocessor.trippipeline.Schema as TripSchema
import org.apache.kafka.clients.admin.NewTopic
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.indices.CreateIndexRequest
import org.elasticsearch.client.indices.PutIndexTemplateRequest
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.xcontent.XContentType

class CompleteSchemaCreatorService
@Inject constructor(private var logger: Logger, private var kafkaAdminClientFactory: AdminClientFactory,
                    private var clientFactory: ClientFactory) {

    fun execute() {
        logger.info("Complete schema creation was executed.")

        // create kafka topics
        kafkaAdminClientFactory.get().use {
            it.createTopics(listOf(NewTopic(NyomSchema.INCOMING_NYOM_TOPIC, 1, 1),
                    NewTopic(TripSchema.GENERATED_NYOM_TOPIC, 1, 1)))
        }

        // create elastic index, index template
        clientFactory.get().use {
            it.indices().putTemplate(PutIndexTemplateRequest(NyomSchema.NYOM_ELASTIC_INDEX).apply {
                create(true)
                patterns(listOf("nyom*"))
                mapping(NyomIndexTemplate.mapping, XContentType.JSON)
                settings(Settings.builder().put("index.number_of_shards", 1)
                        .put("index.number_of_replicas", 1))

            }, RequestOptions.DEFAULT)

            it.indices().putTemplate(PutIndexTemplateRequest(TripSchema.TRIP_ELASTIC_INDEX).apply {
                create(true)
                patterns(listOf("trip*"))
                mapping(TripIndexTemplate.mapping, XContentType.JSON)
                settings(Settings.builder().put("index.number_of_shards", 1)
                        .put("index.number_of_replicas", 1))

            }, RequestOptions.DEFAULT)
            try {
                it.indices().create(CreateIndexRequest(NyomSchema.NYOM_ELASTIC_INDEX), RequestOptions.DEFAULT)
                it.indices().create(CreateIndexRequest(TripSchema.TRIP_ELASTIC_INDEX), RequestOptions.DEFAULT)
            } catch (error: Throwable) {
                logger.error(error)
            }

        }
    }
}
