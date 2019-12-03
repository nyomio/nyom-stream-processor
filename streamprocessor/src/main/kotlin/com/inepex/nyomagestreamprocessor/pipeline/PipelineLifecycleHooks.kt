package com.inepex.nyomagestreamprocessor.pipeline

class PipelineLifecycleHooks {

    companion object {
        var jobExecutionError: ((Throwable) -> Unit)? = null
        var jobIsRunning: (() -> Unit)? = null
        var nyomElasticSinkInsert: (() -> Unit)? = null
        var tripElasticSinkInsert: (() -> Unit)? = null
    }
}
