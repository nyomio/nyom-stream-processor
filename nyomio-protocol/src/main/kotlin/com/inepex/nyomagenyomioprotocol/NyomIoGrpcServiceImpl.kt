package com.inepex.nyomagenyomioprotocol

import com.inepex.nyomagestreamprocessor.api.Pair
import com.inepex.nyomagestreamprocessor.api.incomingnyom.*
import io.grpc.stub.ServerCallStreamObserver
import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory
import javax.inject.Inject

class NyomIoGrpcServiceImpl
@Inject constructor(
        private val nyomStreamBidirectionalHandlerFactory: NyomStreamBidirectionalHandlerFactory
        ) : NyomIoGrpc.NyomIoImplBase() {

    val logger = LoggerFactory.getLogger(NyomIoGrpcServiceImpl::class.java)

    override fun nyom(
            responseObserver: StreamObserver<ServerPushMessage>): StreamObserver<Nyomage> {
        return nyomStreamBidirectionalHandlerFactory.create(
                responseObserver as ServerCallStreamObserver<ServerPushMessage>)
    }

    override fun uploadDetailedRoute(
            responseObserver: StreamObserver<DoneResponse>): StreamObserver<Route> {
        return object : StreamObserver<Route> {
            override fun onNext(value: Route?) {
            }

            override fun onError(t: Throwable?) {
            }

            override fun onCompleted() {
            }
        }
    }

    override fun uploadConfig(request: Config, responseObserver: StreamObserver<DoneResponse>) {
    }

    override fun uploadLogs(
            responseObserver: StreamObserver<DoneResponse>): StreamObserver<LogMessage> {
        return object : StreamObserver<LogMessage> {
            override fun onNext(value: LogMessage?) {
            }

            override fun onError(t: Throwable?) {
            }

            override fun onCompleted() {
            }
        }
    }

    override fun pair(request: Pair.PairRequest,
                      responseObserver: StreamObserver<Pair.PairResponse>) {

    }

}
