package com.inepex.nyomagestreamprocessor.nyompipeline.asyncfunction

import com.inepex.nyomagestreamprocessor.pipeline.Constants
import com.inepex.nyomagestreamprocessor.pipeline.HasTraceId
import org.apache.flink.streaming.api.functions.async.ResultFuture
import org.apache.flink.streaming.api.functions.async.RichAsyncFunction
import org.slf4j.MDC

abstract class BaseRichAsyncFunction<IN: HasTraceId, OUT: HasTraceId> : RichAsyncFunction<IN, OUT>() {
    override fun asyncInvoke(context: IN, resultFuture: ResultFuture<OUT>) {
        MDC.put(Constants.MDC_PARAM_TRACEID, context.traceId)
        asyncInvoke2(context, resultFuture)
    }

    abstract fun asyncInvoke2(context: IN, resultFuture: ResultFuture<OUT>)
}
