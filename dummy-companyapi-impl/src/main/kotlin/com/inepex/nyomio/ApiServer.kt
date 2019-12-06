package com.inepex.nyomio

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.Inject
import com.inepex.nyomio.common.dto.DeviceToObjectMapping
import com.inepex.nyomio.common.dto.GetChangedDeviceToObjectMappingResponse
import com.inepex.nyomio.common.dto.JsonRpcResponse
import com.inepex.nyomio.httpapiserver.HttpApiServer
import com.inepex.nyomio.util.toUtcMillis
import io.netty.handler.codec.http.HttpResponseStatus
import java.time.LocalDateTime

class ApiServer @Inject constructor(
        private var httpApiServer: HttpApiServer
){

    private val mappings = listOf(
            DeviceToObjectMapping(1, "object1", LocalDateTime.of(2019, 1, 1, 8, 10).toUtcMillis(), null),
            DeviceToObjectMapping(2, "object2", LocalDateTime.of(2019, 3, 1, 1, 10).toUtcMillis(), null)
            )

    fun start() {
        httpApiServer.start(8083) { method, requestObj ->
            when (method) {
                Constants.method_GetChangedDeviceToObjectMappingsSince -> {
                    if (requestObj.get("timestampMillis").numberValue() == 0) {
                        HttpResponseStatus.OK to
                                ObjectMapper().writeValueAsString(JsonRpcResponse(GetChangedDeviceToObjectMappingResponse(mappings)))
                    } else {
                        HttpResponseStatus.OK to
                                ObjectMapper().writeValueAsString(JsonRpcResponse(GetChangedDeviceToObjectMappingResponse(emptyList())))
                    }
                }
                else -> HttpResponseStatus.BAD_REQUEST to ""
            }

        }
    }

}
