package com.inepex.nyomagenyomioprotocol

import com.inepex.nyomagestreamprocessor.api.incomingnyom.ServerPushMessage
import io.grpc.stub.ServerCallStreamObserver

interface NyomStreamBidirectionalHandlerFactory {

    fun create(streamObserver: ServerCallStreamObserver<ServerPushMessage>):
            NyomStreamBidirectionalHandler
}
