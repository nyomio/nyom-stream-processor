package com.inepex.nyomio

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.Inject
import com.inepex.nyomio.common.dto.*
import com.inepex.nyomio.config.Configuration
import com.inepex.nyomio.httpapiserver.HttpApiServer
import io.netty.handler.codec.http.HttpResponseStatus

class ApiServer
@Inject constructor(
        private val httpApiServer: HttpApiServer,
        private val configuration: Configuration
        ) {

    fun start() {
        httpApiServer.start(8082) { method, paramsObj ->
            when (method) {
                "getDeviceInfo" -> HttpResponseStatus.OK to ObjectMapper().writeValueAsString(JsonRpcResponse(DeviceInfo(
                        Device(paramsObj.get("deviceId").asLong()), User(1L), Company(1L, configuration.testCompanyApiUrl))))
                else -> HttpResponseStatus.BAD_REQUEST to ""

            }

        }
    }
}
