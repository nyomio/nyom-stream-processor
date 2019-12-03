package com.inepex.nyomagenyomioprotocol.scheduler

import com.google.inject.Singleton
import java.util.concurrent.Executors

@Singleton
class Scheduler {

    private val executor = Executors.newSingleThreadScheduledExecutor()

    fun get() = executor


}
