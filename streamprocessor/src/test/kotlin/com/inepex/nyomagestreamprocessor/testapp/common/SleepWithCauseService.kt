package com.inepex.nyomagestreamprocessor.testapp.common

import com.google.inject.Inject
import com.inepex.nyomagestreamprocessor.logger.Logger

class SleepWithCauseService @Inject constructor(private val logger: Logger) {

    fun execute(durationMs: Long, cause: String) {
        logger.info("Waiting ${durationMs}ms for: $cause")
        Thread.sleep(durationMs)
    }
}
