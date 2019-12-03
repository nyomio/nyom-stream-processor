package com.inepex.nyomagestreamprocessor.logger

import com.google.inject.Singleton
import org.slf4j.LoggerFactory

@Singleton
class Logger {

    val logger = LoggerFactory.getLogger("app")

    fun info(message: String) {
        logger.info(message)
    }

    fun debug(message: String) {
        logger.debug(message)
    }

    fun error(message: String) {
        logger.error(message)
    }

    fun error(message: String, error: Throwable) {
        logger.error(message, error)
    }

    fun error(error: Throwable) {
        logger.error("error", error)
    }
}
