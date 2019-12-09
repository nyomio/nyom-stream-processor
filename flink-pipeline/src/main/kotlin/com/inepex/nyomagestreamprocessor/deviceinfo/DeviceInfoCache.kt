package com.inepex.nyomagestreamprocessor.deviceinfo

import com.github.benmanes.caffeine.cache.AsyncLoadingCache
import com.github.benmanes.caffeine.cache.Caffeine
import com.inepex.nyomagestreamprocessor.common.dto.DeviceInfo
import com.inepex.nyomagestreamprocessor.devicemanagerapi.DeviceManagerApi
import com.inepex.nyomagestreamprocessor.logger.Logger
import com.inepex.nyomagestreamprocessor.scheduler.Scheduler
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

class DeviceInfoCache constructor(
        scheduler: Scheduler,
        private val logger: Logger, private val deviceManagerApi: DeviceManagerApi) {

    private val cache: AsyncLoadingCache<String, DeviceInfo> = Caffeine.newBuilder()
            .executor(scheduler.get())
            .expireAfterAccess(1, TimeUnit.HOURS)
            .refreshAfterWrite(1, TimeUnit.DAYS)
            .buildAsync<String, DeviceInfo> { key, executor ->
//                logger.info("DeviceInfoCache async $key")
                deviceManagerApi.getDeviceInfo(key, executor)
            }

    fun getDeviceInfo(deviceId: String): CompletableFuture<DeviceInfo> {
//        logger.info("DeviceInfoCache.getDeviceInfo $deviceId")
        return cache.get(deviceId)
    }
//    fun getDeviceInfo(deviceId: Long) = Single.create<DeviceInfo> { emitter ->
//        cache.get(deviceId)
//                .handleAsync { deviceInfo, error ->
//                    if (error == null) {
//                        emitter.onSuccess(deviceInfo)
//                    } else {
//                        emitter.onError(error)
//                    }
//                }
//    }

    fun invalidate(deviceId: String) {
        cache.synchronous().invalidate(deviceId)
    }

}
