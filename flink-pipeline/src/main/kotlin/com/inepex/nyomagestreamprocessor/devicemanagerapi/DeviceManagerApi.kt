package com.inepex.nyomagestreamprocessor.devicemanagerapi

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.inepex.nyomagestreamprocessor.common.dto.DeviceInfo
import com.inepex.nyomagestreamprocessor.config.Configuration
import com.inepex.nyomagestreamprocessor.httpclient.HttpClient
import com.inepex.nyomagestreamprocessor.logger.Logger
import com.inepex.nyomagestreamprocessor.scheduler.Scheduler
import java.util.concurrent.CompletableFuture
import org.slf4j.MDC
import java.util.concurrent.Executor
import java.util.function.Function

class DeviceManagerApi constructor(
        private val scheduler: Scheduler,
        private val httpClient: HttpClient,
        private val logger: Logger,
        private val config: Configuration) {

    fun getDeviceInfo(deviceId: String, executor: Executor): CompletableFuture<DeviceInfo> {
        val mdcContext = MDC.getCopyOfContextMap()
//        logger.info("DeviceManagerApi.getDeviceInfo $deviceId")
//        return asyncHttpClient(/*config().setEventLoopGroup(NioEventLoopGroup(1, executor))*/).use {
        return httpClient.get().preparePost(config.deviceManagerUrl)
                .setBody(ObjectMapper().writeValueAsString(mapOf("jsonrpc" to "2.0", "method" to "getDeviceInfo",
                        "params" to mapOf("deviceId" to deviceId))))
                .execute()
                .toCompletableFuture()
                .thenApplyAsync(Function {
                    MDC.setContextMap(mdcContext)
//                    logger.info("DeviceManagerApi asyncResponse $deviceId")
                    val mapper = ObjectMapper().registerModule(KotlinModule())
                    val rootNode = mapper.readTree(it.responseBody)
                    // TODO error handling
                    mapper.treeToValue(rootNode.get("result"), DeviceInfo::class.java)

                }, scheduler.get())
    }
}
