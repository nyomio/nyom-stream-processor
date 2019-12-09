package com.inepex.nyomagestreamprocessor.scheduler

import com.google.common.util.concurrent.ThreadFactoryBuilder
import java.util.concurrent.Executors

class Scheduler {

    private val executor = Executors.newSingleThreadScheduledExecutor(
            ThreadFactoryBuilder().setNameFormat("nyomage-scheduler-%d").build())

    fun get() = executor


}
