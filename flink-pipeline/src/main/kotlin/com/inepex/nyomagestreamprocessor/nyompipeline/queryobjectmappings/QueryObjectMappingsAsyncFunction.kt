package com.inepex.nyomagestreamprocessor.nyompipeline.queryobjectmappings

import com.inepex.nyomagestreamprocessor.api.incomingnyom.getTimestamp
import com.inepex.nyomagestreamprocessor.schema.elastic.Mapping
import com.inepex.nyomagestreamprocessor.pipeline.Dependencies
import com.inepex.nyomagestreamprocessor.nyompipeline.asyncfunction.BaseRichAsyncFunction
import com.inepex.nyomagestreamprocessor.nyompipeline.context.Step2_QueryDeviceInfo
import com.inepex.nyomagestreamprocessor.nyompipeline.context.Step3_QueryObjectMapping
import org.apache.flink.streaming.api.functions.async.ResultFuture
import org.slf4j.MDC
import java.util.*

class QueryObjectMappingsAsyncFunction : BaseRichAsyncFunction<Step2_QueryDeviceInfo, Step3_QueryObjectMapping>(){

    override fun asyncInvoke2(context: Step2_QueryDeviceInfo, resultFuture: ResultFuture<Step3_QueryObjectMapping>) {
        if (context.organization == null || context.organization.apiUrl.isEmpty()) {
            resultFuture.complete(Collections.singleton(Step3_QueryObjectMapping(context,
                    Mapping())))
        } else {
            val mdcContext = MDC.getCopyOfContextMap()
            Dependencies.get().deviceToObjectMappingCache.getMappingsForDeviceAtTime(
                    context.organization.id, context.organization.apiUrl, context.device.id,
                    context.incomingNyomEntry.getTimestamp()).thenAcceptAsync {
                MDC.setContextMap(mdcContext)

                val mapping = Mapping()
                when {
                    it.size == 1 -> {mapping.objectId1 = it[0].objectId}
                    it.size == 2 -> {
                        mapping.objectId1 = it[0].objectId
                        mapping.objectId2 = it[1].objectId
                    }
                    it.size == 3 -> {
                        mapping.objectId1 = it[0].objectId
                        mapping.objectId2 = it[1].objectId
                        mapping.objectId3 = it[2].objectId
                    }
                }
                resultFuture.complete(Collections.singleton(Step3_QueryObjectMapping(context, mapping)))
            }
        }

    }
}
