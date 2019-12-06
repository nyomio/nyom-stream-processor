package com.inepex.nyomio.httpapiserver

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.Inject
import com.inepex.nyomio.logger.Logger
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.*
import io.netty.util.CharsetUtil


@ChannelHandler.Sharable
class HttpObjectInboundHandler @Inject constructor(
        private val logger: Logger
) : SimpleChannelInboundHandler<FullHttpRequest>() {

    private var handler: ((method:String, requestObj: JsonNode)-> Pair<HttpResponseStatus, String>)? = null

    fun setHandler(handler: (method:String, requestObj: JsonNode)-> Pair<HttpResponseStatus, String>) {
        this.handler = handler
    }

    override fun channelRead0(ctx: ChannelHandlerContext, msg: FullHttpRequest) {
        logger.info("Incoming request: ${msg.content().toString(CharsetUtil.UTF_8)}")
        val mapper = ObjectMapper()
        val requestObj = mapper.readTree(msg.content().toString(CharsetUtil.UTF_8))
        val response = if (requestObj.has("jsonrpc")
                && requestObj.get("jsonrpc").textValue() == "2.0") {
            handler!!(requestObj.get("method").textValue(), requestObj.get("params")).let {
                DefaultFullHttpResponse(msg.protocolVersion(), it.first, Unpooled.copiedBuffer(
                            it.second, CharsetUtil.UTF_8))
            }
        } else {
            DefaultFullHttpResponse(msg.protocolVersion(), HttpResponseStatus.BAD_REQUEST)
        }

        val keepAlive = HttpUtil.isKeepAlive(msg)
        response.headers()
                .set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN)
                .setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes())

        if (keepAlive) {
            if (!msg.protocolVersion().isKeepAliveDefault()) {
                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE)
            }
        } else {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE)
        }

        val f = ctx.writeAndFlush(response)

        if (!keepAlive) {
            f.addListener(ChannelFutureListener.CLOSE)
        }
    }

}
