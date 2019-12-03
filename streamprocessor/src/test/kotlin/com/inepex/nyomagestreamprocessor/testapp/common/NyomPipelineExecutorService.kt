package com.inepex.nyomagestreamprocessor.testapp.common

import com.google.inject.Inject
import com.inepex.nyomagestreamprocessor.config.Configuration
import com.inepex.nyomagestreamprocessor.pipeline.Dependencies
import com.inepex.nyomagestreamprocessor.pipeline.PipelineLifecycleHooks
import com.inepex.nyomagestreamprocessor.pipeline.PipelinesExecutorService
import io.reactivex.Single
import io.reactivex.SingleEmitter
import java.util.concurrent.atomic.AtomicInteger

class NyomPipelineExecutorService
@Inject constructor(
        private var pipelines: PipelinesExecutorService,
        private var configuration: Configuration) {

    private val numberOfNyomInserts = AtomicInteger(0)
    private var expectedNumberOfNyomInserts: Int? = null
    private val numberOfTripInserts = AtomicInteger(0)
    private var expectedNumberOfTripInserts: Int? = null
    private var waitNyomInsertsCallback: SingleEmitter<Boolean>? = null
    private var waitTripInsertsCallback: SingleEmitter<Boolean>? = null
    private var thread: Thread? = null

    fun execute(): Single<Boolean> {
        return Single.create<Boolean> { emitter->
            Dependencies.initWithConfig(configuration)
            PipelineLifecycleHooks.jobIsRunning = {
                emitter.onSuccess(true)
            }
            PipelineLifecycleHooks.jobExecutionError = {
                if (it !is InterruptedException) {
                    emitter.onError(RuntimeException("Job failed."))
                }
            }
            PipelineLifecycleHooks.nyomElasticSinkInsert = {
                numberOfNyomInserts.incrementAndGet()
                fireNyomInsertsCallback()
            }
            PipelineLifecycleHooks.tripElasticSinkInsert = {
                numberOfTripInserts.incrementAndGet()
                fireTripInsertsCallback()
            }
            thread = Thread(Runnable { pipelines.execute(true) })
            thread?.start()


        }
    }

    private fun fireNyomInsertsCallback() {
        if (waitNyomInsertsCallback != null && numberOfNyomInserts.get() >= expectedNumberOfNyomInserts!!) {
            waitNyomInsertsCallback!!.onSuccess(true)
        }
    }

    fun waitNyomInserts(expectedInserts: Int) = Single.create<Boolean> {
        expectedNumberOfNyomInserts = expectedInserts
        waitNyomInsertsCallback = it
        fireNyomInsertsCallback()
    }

    private fun fireTripInsertsCallback() {
        if (waitTripInsertsCallback != null && numberOfTripInserts.get() >= expectedNumberOfTripInserts!!) {
            waitTripInsertsCallback!!.onSuccess(true)
        }
    }
    fun waitTripInserts(expectedInserts: Int) = Single.create<Boolean> {
        expectedNumberOfTripInserts = expectedInserts
        waitTripInsertsCallback = it
        fireTripInsertsCallback()
    }

    fun shutdown() {
        thread?.interrupt()
    }

}
