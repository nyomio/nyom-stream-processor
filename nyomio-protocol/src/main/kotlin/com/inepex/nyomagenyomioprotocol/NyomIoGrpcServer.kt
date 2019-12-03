package com.inepex.nyomagenyomioprotocol

import com.inepex.nyomagenyomioprotocol.config.Configuration
import com.inepex.nyomagenyomioprotocol.logger.Logger
import io.grpc.*
import io.grpc.ServerCall.Listener
import java.io.File
import java.io.IOException
import java.net.SocketAddress
import java.util.*
import javax.inject.Inject

class NyomIoGrpcServer
@Inject constructor(
        private val logger: Logger,
        private val nyomIoGrpcServiceImpl: NyomIoGrpcServiceImpl,
        private val configuration: Configuration
) {

    companion object {
        val USER_IP: Context.Key<SocketAddress> = Context.key("IP")
    }

    private var server: Server? = null

    private val remoteAddressInterceptor = object : ServerInterceptor {
        override fun <ReqT, RespT> interceptCall(call: ServerCall<ReqT, RespT>,
                                                 headers: Metadata, next: ServerCallHandler<ReqT, RespT>): Listener<ReqT> {
            val attrs = call.attributes

            val remoteIp = Objects.requireNonNull<SocketAddress>(
                    attrs.get<SocketAddress>(Grpc.TRANSPORT_ATTR_REMOTE_ADDR))

            return Contexts.interceptCall(
                    Context.current().withValue(USER_IP, remoteIp), call, headers, next)
        }
    }

    fun start() {
        try {
            val serverBuilder = ServerBuilder.forPort(configuration.grpcListenPort)
                    .addService(this.nyomIoGrpcServiceImpl)
                    .intercept(remoteAddressInterceptor)

            if (configuration.grpcUseTls) {
                serverBuilder.useTransportSecurity(File(configuration.grpcCertChainPath),
                        File(configuration.grpcPrivateKeyPath))
            }

            server = serverBuilder.build()
                    .start()
            logger.info("GRPC server started, listening on ${configuration.grpcListenPort}")
            server?.awaitTermination()
        } catch (e: IOException) {
            logger.error("FATAL!! - NyomIo GRPC server could not be started", e)
        }

    }

    fun shutdown() {
        server?.shutdown()
    }

    fun shutdownNow() {
        server?.shutdownNow()
        server?.awaitTermination()
    }

}


