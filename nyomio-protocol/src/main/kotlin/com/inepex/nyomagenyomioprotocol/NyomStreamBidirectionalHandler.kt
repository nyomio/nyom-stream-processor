package com.inepex.nyomagenyomioprotocol

import com.google.inject.assistedinject.Assisted
import com.inepex.nyomagenyomioprotocol.logger.Logger
import com.inepex.nyomagenyomioprotocol.statuslogger.StatusLogger
import com.inepex.nyomagestreamprocessor.api.incomingnyom.Nyomage
import com.inepex.nyomagestreamprocessor.api.incomingnyom.ServerPushMessage
import io.grpc.Status
import io.grpc.stub.ServerCallStreamObserver
import io.grpc.stub.StreamObserver
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

class NyomStreamBidirectionalHandler @Inject constructor(
        private val statusLogger: StatusLogger,
        private val logger: Logger,
        @Assisted val responseObserver: ServerCallStreamObserver<ServerPushMessage>,
        private val nyomageProcessor: NyomageProcessor
) : StreamObserver<Nyomage> {

    private val nyomageCount = AtomicInteger(0)
    private var nativeId = ""
    private var wasStreamReady = false
    private var isMessageProcessingEnabled = true
    private var isRequestExecuted = false

    init {
        statusLogger.incActiveConnections()
        responseObserver.disableAutoInboundFlowControl()
        responseObserver.setOnReadyHandler {
            synchronized(this) {
                if (responseObserver.isReady && !wasStreamReady) {
                    wasStreamReady = true
                    requestNextMessageIfPossible()
                }
            }
        }

//        val messageProcessingStatusHandler: (Boolean) -> Unit = {
//            synchronized(this) {
//                isMessageProcessingEnabled = it
//                requestNextMessageIfPossible()
//            }
//        }
//        queuingMessageProcessorSubscription = queuingMessageProcessorFailureHandler.subscribeEvents(
//                messageProcessingStatusHandler)
//        queuingMessageProcessorQueueSizeLimitHandlerSubscription = queueingMessageProcessorQueueSizeLimitHandler.subscribeEvents(
//                messageProcessingStatusHandler
//        )
    }

    override fun onNext(nyomage: Nyomage) {
        synchronized(this) {
            isRequestExecuted = false
        }
        checkNativeId(nyomage)
        logger.debug(
                "Nyomage arrived. IP: ${NyomIoGrpcServer.USER_IP.get()} " +
                        "NativeId: ${if (nativeId == "") nyomage.nativeId else nativeId} " +
                        "SeqNum: ${nyomage.seqNum} " +
                        "# of locations: ${nyomage.locationCount} " +
                        "# of events: ${nyomage.eventsCount} " +
                        "# of statuses: ${nyomage.statusCount} "
        )
        nyomageProcessor.processNyomage(nativeId, nyomage).subscribe({
            responseObserver.onNext(ServerPushMessage.newBuilder().setNyomageResponseSeqNum(
                    nyomage.seqNum).build())
            requestNextMessageIfPossible()
        }, {
            if (it != null) {
                responseObserver.onError(it)
            }
        })
    }

    private fun checkNativeId(nyomage: Nyomage) {
        if (nativeId.isBlank()) {
            if (nyomage.nativeId.isBlank()) {
                val error = "First nyomage must contain nativeId"
                logger.error(error)
                throw Exception(error)
            }
            nativeId = nyomage.nativeId
        }
    }

    @Synchronized
    private fun requestNextMessageIfPossible() {
        if (isMessageProcessingEnabled && !isRequestExecuted && responseObserver.isReady) {
            responseObserver.request(1)
            isRequestExecuted = true
        } else if (!responseObserver.isReady) {
            wasStreamReady = false
        }
    }

    override fun onError(t: Throwable) {
        val s = Status.fromThrowable(t)
        if (s.getCode() != Status.Code.CANCELLED) {
            logger.error("Error occurred on Nyomage stream", t)
        } else {
            logger.info("Nyomage stream was cancelled. Message: ${t.message}")
        }
        statusLogger.decrActiveConnections()
    }

    override fun onCompleted() {
        logger.info("Client ended Nyomage stream")
        statusLogger.decrActiveConnections()

    }

}
