package com.inepex.nyomagestreamprocessor.pipeline

import com.google.inject.Inject
import com.google.inject.Singleton
import com.inepex.nyomagestreamprocessor.logger.Logger
import com.inepex.nyomagestreamprocessor.nyompipeline.Pipeline
import org.apache.flink.api.common.restartstrategy.RestartStrategies
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.environment.CheckpointConfig
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment

@Singleton
class PipelinesExecutorService @Inject constructor(
        private var logger: Logger,
        private var env: StreamExecutionEnvironment,
        private val nyomPipeline: Pipeline,
        private val tripPipeline: com.inepex.nyomagestreamprocessor.trippipeline.Pipeline
){

    fun execute(setKafkaOffsetToEarliest: Boolean = false) {
        env.streamTimeCharacteristic = TimeCharacteristic.ProcessingTime
        env.enableCheckpointing(30000)
        env.checkpointConfig.enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION)

        logger.info("Pipeline.execute")

        nyomPipeline.execute(env, setKafkaOffsetToEarliest)
        tripPipeline.execute(env, setKafkaOffsetToEarliest)

        try {
            env.execute("nyomjob")
        } catch (error: Throwable) {
            PipelineLifecycleHooks.jobExecutionError?.invoke(error)
            throw error
        }
    }
}
