package com.inepex.nyomagenyomioprotocol.statuslogger

import com.google.inject.Inject
import com.google.inject.Singleton
import com.inepex.nyomagenyomioprotocol.logger.Logger
import com.inepex.nyomagenyomioprotocol.scheduler.Scheduler
import java.util.concurrent.TimeUnit

data class StatusForLogging(
        val numberOfActiveConnections: Long = 0L,
        val numberOfReceivedNyom: Long = 0L,
        val numberOfProcessedNyom: Long = 0L
)

@Singleton
class StatusLogger
@Inject constructor(scheduler: Scheduler,
                    private val logger: Logger) {

    private var numberOfActiveConnections: Long = 0L
    private var numberOfReceivedNyom: Long = 0L
    private var numberOfProcessedNyom: Long = 0L

    init {
        scheduler.get().scheduleAtFixedRate(
                { logStatus() },
                1,
                1,
                TimeUnit.MINUTES
        )
    }

    private fun logStatus() {
        getStatusAndReset().let {
            logger.info("Status: $it ")
        }
    }

    @Synchronized
    private fun getStatusAndReset(): StatusForLogging {
        val status = StatusForLogging(
                numberOfActiveConnections,
                numberOfReceivedNyom,
                numberOfProcessedNyom
        )

        numberOfReceivedNyom = 0L
        numberOfProcessedNyom = 0L

        return status
    }

    @Synchronized
    fun incActiveConnections() {
        numberOfActiveConnections++
    }

    @Synchronized
    fun decrActiveConnections() {
        numberOfActiveConnections--
    }

    @Synchronized
    fun incReceivedNyom(increment: Long = 1) {
        numberOfReceivedNyom += increment
    }


    fun incrProcessedNyom(increment: Long = 1) {
        numberOfProcessedNyom += increment
    }

}
