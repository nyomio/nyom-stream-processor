package com.inepex.nyomagestreamprocessor.testapp.common

import com.google.inject.AbstractModule
import com.inepex.nyomagestreamprocessor.GuiceModule

class TestAppGuiceModule : AbstractModule() {
    override fun configure() {
        System.setProperty("LogstashTcpSocketAppender", "localhost:4560")
        install(GuiceModule("tiborsomodi", startLocalFlinkEnv = true,
                parseEnvVarsForConfig = false))
    }
}
