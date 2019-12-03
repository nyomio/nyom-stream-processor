package com.inepex.nyomagestreamprocessor.trippipeline

import com.google.inject.Inject
import com.google.inject.Singleton
import com.inepex.nyomagestreamprocessor.logger.Logger
import com.inepex.nyomagestreamprocessor.pipeline.Dependencies
import com.inepex.nyomagestreamprocessor.pipeline.KafkaSourcePropertiesProvider
import com.inepex.nyomagestreamprocessor.trippipeline.context.Step1_NyomKafkaSource
import com.inepex.nyomagestreamprocessor.trippipeline.maptotrip.MapToTripProcessFunction
import com.inepex.nyomagestreamprocessor.trippipeline.nyomsource.NyomKafkaSourceFunction
import com.inepex.nyomagestreamprocessor.trippipeline.nyomsource.ProtoBufDeserializationSchema
import com.inepex.nyomagestreamprocessor.trippipeline.tripsink.TripElasticSinkFactory
import org.apache.flink.api.java.functions.KeySelector
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment

/**
 * Functions used in pipeline are intentionally not injected. Flink creates new instances by
 * deserialization.
 */
@Singleton
class Pipeline @Inject constructor(
        private var logger: Logger,
        private var env: StreamExecutionEnvironment
) {

    fun execute(env: StreamExecutionEnvironment, setKafkaOffsetToEarliest: Boolean = false) {

        env
                .addSource(NyomKafkaSourceFunction(ProtoBufDeserializationSchema(),
                        KafkaSourcePropertiesProvider(Dependencies.get().config)).apply {
                    if (setKafkaOffsetToEarliest) setStartFromEarliest()
                })
                .disableChaining()
                .keyBy(KeySelector<Step1_NyomKafkaSource, Long> {
                    it.nyom.deviceId
                })
                .process(MapToTripProcessFunction())
                .disableChaining()
                .addSink(TripElasticSinkFactory().get())


        // kafka source generatedNyom
        // keyBy xy
        // map function to generate trips
        // async to query matching POIs by mapping for a new trip
        // (categorize trip)
        // kafka sink trip
        // elastic sink tripByXy

    }
}
