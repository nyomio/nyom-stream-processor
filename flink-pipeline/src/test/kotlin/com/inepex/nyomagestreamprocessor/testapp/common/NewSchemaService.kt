package com.inepex.nyomagestreamprocessor.testapp.common

import com.google.inject.Inject
import com.inepex.nyomagestreamprocessor.elastic.ClientFactory
import com.inepex.nyomagestreamprocessor.kafka.AdminClientFactory
import com.inepex.nyomagestreamprocessor.logger.Logger
import com.inepex.nyomagestreamprocessor.nyompipeline.Schema as NyomSchema
import com.inepex.nyomagestreamprocessor.trippipeline.Schema as TripSchema
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.common.errors.UnknownTopicOrPartitionException
import org.elasticsearch.ElasticsearchStatusException
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.indices.CreateIndexRequest
import java.util.concurrent.ExecutionException

class NewSchemaService
@Inject constructor(
        private var sleepWithCauseService: SleepWithCauseService,
        private var logger: Logger,
        private var kafkaAdminClientFactory: AdminClientFactory,
        private var elasticClientFactory: ClientFactory
) {

    fun execute() {
        kafkaAdminClientFactory.get().use {
            try {
                it.deleteTopics(listOf(NyomSchema.INCOMING_NYOM_TOPIC, TripSchema.GENERATED_NYOM_TOPIC)).all().get()
            } catch (error: Throwable) {
                if (!(error is ExecutionException && error.cause is UnknownTopicOrPartitionException)) {
                    error.printStackTrace()
                }
            }
            sleepWithCauseService.execute(500, "Kafka topic deletion")

            it.createTopics(listOf(NewTopic(NyomSchema.INCOMING_NYOM_TOPIC, 1, 1),
                    NewTopic(TripSchema.GENERATED_NYOM_TOPIC, 1, 1))).all().get()
        }

        elasticClientFactory.get().use {
            try {
                it.indices().delete(DeleteIndexRequest(NyomSchema.NYOM_ELASTIC_INDEX), RequestOptions.DEFAULT)
                it.indices().delete(DeleteIndexRequest(TripSchema.TRIP_ELASTIC_INDEX), RequestOptions.DEFAULT)
                it.indices().create(CreateIndexRequest(NyomSchema.NYOM_ELASTIC_INDEX), RequestOptions.DEFAULT)
                it.indices().create(CreateIndexRequest(TripSchema.TRIP_ELASTIC_INDEX), RequestOptions.DEFAULT)
            } catch (error: ElasticsearchStatusException) {
                if (error.message == "Elasticsearch exception [type=index_not_found_exception, reason=no such index [nyom]]") {
                    logger.error("Before running any tests you have to execute com.inepex.nyomagestreamprocessor.AppKt once, to create schemas.")
                }
                throw error
            }
        }
    }
}
