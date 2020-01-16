package com.inepex.nyomagestreamprocessor.nyompipeline.querydeviceinfo

import com.github.benmanes.caffeine.cache.AsyncLoadingCache
import com.github.benmanes.caffeine.cache.Caffeine
import com.inepex.nyomagestreamprocessor.jwt.SystemUserJwtTokenGenerator
import com.inepex.nyomagestreamprocessor.logger.Logger
import com.inepex.nyomagestreamprocessor.scheduler.Scheduler
import nyomio.app.core.devicemanager.getdeviceinfo.DeviceInfo
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

class DeviceInfoCache constructor(
        scheduler: Scheduler,
        private val logger: Logger, private val getDeviceInfoHttpClient: GetDeviceInfoHttpClient,
        private val jwtTokenGenerator: SystemUserJwtTokenGenerator) {

    private val cache: AsyncLoadingCache<String, DeviceInfo> = Caffeine.newBuilder()
            .executor(scheduler.get())
            .expireAfterAccess(1, TimeUnit.HOURS)
            .refreshAfterWrite(1, TimeUnit.DAYS)
            .buildAsync<String, DeviceInfo> { key, executor ->
                val result = CompletableFuture<DeviceInfo>()
                getDeviceInfoHttpClient.execute(jwtTokenGenerator.generate(), key).subscribe({
                    result.complete(it)
                }, {
                    result.completeExceptionally(it)
                })
                result
            }

    fun getDeviceInfo(deviceId: String): CompletableFuture<DeviceInfo> {
        return cache.get(deviceId)
    }

    fun invalidate(deviceId: String) {
        cache.synchronous().invalidate(deviceId)
    }

}
