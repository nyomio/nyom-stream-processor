package com.inepex.nyomagestreamprocessor.nyompipeline

import com.google.inject.Inject
import com.google.inject.Singleton
import com.inepex.nyomagestreamprocessor.logger.Logger
import com.inepex.nyomagestreamprocessor.pipeline.HasTraceId
import com.inepex.nyomagestreamprocessor.nyompipeline.context.Step3_QueryObjectMapping
import com.inepex.nyomagestreamprocessor.nyompipeline.mapincomingnyom.MapIncomingNyomWindowFunction
import com.inepex.nyomagestreamprocessor.nyompipeline.nyomsource.NyomKafkaSourceFunction
import com.inepex.nyomagestreamprocessor.nyompipeline.nyomsink.NyomElasticSinkFactory
import com.inepex.nyomagestreamprocessor.nyompipeline.nyomsink.NyomKafkaSink
import com.inepex.nyomagestreamprocessor.nyompipeline.nyomsource.ProtoBufDeserializationSchema
import com.inepex.nyomagestreamprocessor.nyompipeline.querydeviceinfo.QueryDeviceInfoAsyncFunction
import com.inepex.nyomagestreamprocessor.nyompipeline.queryobjectmappings.QueryObjectMappingsAsyncFunction
import com.inepex.nyomagestreamprocessor.pipeline.Dependencies
import com.inepex.nyomagestreamprocessor.pipeline.KafkaSourcePropertiesProvider
import org.apache.flink.api.java.functions.KeySelector
import org.apache.flink.streaming.api.datastream.AsyncDataStream
import org.apache.flink.streaming.api.datastream.DataStream
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.api.functions.async.AsyncFunction
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.slf4j.MDC
import java.util.concurrent.TimeUnit

/**
 * Functions used in pipeline are intentionally not injected. Flink creates new instances by
 * deserialization.
 */
@Singleton
class Pipeline @Inject constructor(
        private var logger: Logger
) {

    fun execute(env: StreamExecutionEnvironment, setKafkaOffsetToEarliest: Boolean = false) {
        env.addSource(NyomKafkaSourceFunction(ProtoBufDeserializationSchema(),
                KafkaSourcePropertiesProvider(Dependencies.get().config)).apply { if (setKafkaOffsetToEarliest) setStartFromEarliest() }).name("")
                .async(QueryDeviceInfoAsyncFunction())
                .disableChaining()
                .async(QueryObjectMappingsAsyncFunction())
                .disableChaining()
                .keyBy(KeySelector<Step3_QueryObjectMapping, String> {
                    MDC.put(com.inepex.nyomagestreamprocessor.pipeline.Constants.MDC_PARAM_TRACEID, it.traceId)
                    Dependencies.get().logger.debug("keyBy applied")
                    it.incomingNyomEntry.nativeId
                })
                .window(TumblingProcessingTimeWindows.of(Time.milliseconds(Constants.PROCESSING_TIME_WINDOW_LENGTH_MS)))
                .process(MapIncomingNyomWindowFunction())
                .disableChaining()
                .apply {
                    addSink(NyomKafkaSink())
                    addSink(NyomElasticSinkFactory().get())
                }
    }
}

fun <IN : HasTraceId, OUT : HasTraceId> DataStream<IN>.async(function: AsyncFunction<IN, OUT>): SingleOutputStreamOperator<OUT> {
    return AsyncDataStream.orderedWait(this, function, 300, TimeUnit.SECONDS, 1000)
}
