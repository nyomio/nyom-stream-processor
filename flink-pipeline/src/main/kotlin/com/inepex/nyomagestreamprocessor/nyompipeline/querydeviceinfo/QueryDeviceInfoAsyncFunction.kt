package com.inepex.nyomagestreamprocessor.nyompipeline.querydeviceinfo

import com.inepex.nyomagestreamprocessor.pipeline.Dependencies
import com.inepex.nyomagestreamprocessor.nyompipeline.asyncfunction.BaseRichAsyncFunction
import com.inepex.nyomagestreamprocessor.nyompipeline.context.Step1_NyomKafkaSource
import com.inepex.nyomagestreamprocessor.nyompipeline.context.Step2_QueryDeviceInfo
import org.apache.flink.streaming.api.functions.async.ResultFuture
import org.slf4j.MDC
import java.util.*

class QueryDeviceInfoAsyncFunction : BaseRichAsyncFunction<Step1_NyomKafkaSource, Step2_QueryDeviceInfo>() {

    override fun asyncInvoke2(context: Step1_NyomKafkaSource, resultFuture: ResultFuture<Step2_QueryDeviceInfo>) {

        val mdcContext = MDC.getCopyOfContextMap()
        Dependencies.get().deviceInfoCache.getDeviceInfo(context.incomingNyomEntry.nativeId).thenAcceptAsync {
            MDC.setContextMap(mdcContext)
            resultFuture.complete(Collections.singleton(Step2_QueryDeviceInfo(context.traceId,
                    context.incomingNyomEntry, it.device, it.organization)))

        }
    }

}

