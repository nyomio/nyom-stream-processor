package com.inepex.nyomagestreamprocessor.basicdevicemanager

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.Inject
import com.inepex.nyomagestreamprocessor.common.dto.Company
import com.inepex.nyomagestreamprocessor.common.dto.Device
import com.inepex.nyomagestreamprocessor.common.dto.DeviceInfo
import com.inepex.nyomagestreamprocessor.common.dto.User
import com.inepex.nyomagestreamprocessor.config.Configuration
import com.inepex.nyomagestreamprocessor.httpapiserver.HttpApiServer
import com.inepex.nyomagestreamprocessor.jsonrpc.JsonRpcResponse
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
