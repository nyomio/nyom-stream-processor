package com.inepex.nyomagestreamprocessor.companyapi

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.inepex.nyomagestreamprocessor.common.dto.GetChangedDeviceToObjectMappingResponse
import com.inepex.nyomagestreamprocessor.config.Configuration
import com.inepex.nyomagestreamprocessor.httpclient.HttpClient
import com.inepex.nyomagestreamprocessor.logger.Logger
import com.inepex.nyomagestreamprocessor.scheduler.Scheduler
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.function.Function

class GetChangedDeviceToObjectMappingSinceApi constructor(
        private val scheduler: Scheduler,
        private val httpClient: HttpClient,
        private val logger: Logger,
        private val configuration: Configuration
) {

    fun execute(apiUrl: String, sinceTimestampMs: Long, executor: Executor): CompletableFuture<GetChangedDeviceToObjectMappingResponse> {
//        logger.info("GetChangedDeviceToObjectMappingSinceApi: $apiUrl")
//        return asyncHttpClient(/*config().setEventLoopGroup(NioEventLoopGroup(1, executor))*/).use {
        return httpClient.get()
                .preparePost(apiUrl)
                .setBody(ObjectMapper().writeValueAsString(mapOf("jsonrpc" to "2.0",
                        "method" to Constants.method_GetChangedDeviceToObjectMappingsSince,
                        "params" to mapOf(Constants.param_timestampMillis to sinceTimestampMs))))
                .execute()
                .toCompletableFuture()
                .thenApplyAsync(Function {
                    //                    logger.info("GetChangedDeviceToObjectMappingSinceApi: response arrived: ${it.responseBody} status: ${it.statusText}")
                    val mapper = ObjectMapper().registerModule(KotlinModule())
                    val rootNode = mapper.readTree(it.responseBody)
                    // TODO error handling
                    mapper.treeToValue(rootNode.get("result"), GetChangedDeviceToObjectMappingResponse::class.java)
                }, scheduler.get())
    }
}
