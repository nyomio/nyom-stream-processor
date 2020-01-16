package com.inepex.nyomagestreamprocessor.nyompipeline.querydeviceinfo

import io.micronaut.http.annotation.CookieValue
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.client.annotation.Client
import io.reactivex.Single
import nyomio.app.core.devicemanager.getdeviceinfo.DeviceInfo
import nyomio.app.core.devicemanager.getdeviceinfo.GetDeviceInfoDefinition

@Client("\${nyomApp.deviceManagerUrl}")
interface GetDeviceInfoHttpClient: GetDeviceInfoDefinition {

    override fun execute(@CookieValue JWT: String, nativeId: String): Single<DeviceInfo>
}
