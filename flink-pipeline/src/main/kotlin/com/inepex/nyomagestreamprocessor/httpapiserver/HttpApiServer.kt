package com.inepex.nyomagestreamprocessor.httpapiserver

import com.fasterxml.jackson.databind.JsonNode
import com.google.inject.Inject
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.HttpResponseStatus
import io.netty.handler.codec.http.HttpServerCodec
import io.netty.handler.codec.http.HttpServerExpectContinueHandler

interface HttpApiMethodResponseHandler {
    fun handle(method: String, requestObject: JsonNode): Pair<HttpResponseStatus, String>
}

class HttpApiServer @Inject constructor(private val httpObjectInboundHandler: HttpObjectInboundHandler) {

    fun start(port:Int, handler: (method:String, requestObj: JsonNode)-> Pair<HttpResponseStatus, String>) {
        val bossGroup = NioEventLoopGroup(1)
        val workerGroup = NioEventLoopGroup()
        try {
            val b = ServerBootstrap().apply {
                option(ChannelOption.SO_BACKLOG, 1024)
                group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel::class.java)
                        .childHandler(object : ChannelInitializer<SocketChannel>() {
                            override fun initChannel(ch: SocketChannel) {
                                ch.pipeline().apply {
                                    addLast(HttpServerCodec())
                                    addLast(HttpServerExpectContinueHandler())
                                    addLast(HttpObjectAggregator(512 * 1024))
                                    addLast(httpObjectInboundHandler.apply { setHandler(handler) })
                                }
                            }
                        })
            }
            val ch = b.bind(port).sync().channel()

            ch.closeFuture().sync()
        } finally {
            bossGroup.shutdownGracefully()
            workerGroup.shutdownGracefully()
        }
    }
}
