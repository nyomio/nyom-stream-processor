package com.inepex.nyomagestreamprocessor.pipeline

import org.apache.commons.lang3.RandomStringUtils


class GenerateTraceIdService {
    fun execute()= "" + System.currentTimeMillis() + RandomStringUtils.randomAlphanumeric(6)
}
