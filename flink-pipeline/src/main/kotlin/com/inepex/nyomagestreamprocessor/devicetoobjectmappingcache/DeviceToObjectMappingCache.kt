package com.inepex.nyomagestreamprocessor.devicetoobjectmappingcache

import com.github.benmanes.caffeine.cache.AsyncCacheLoader
import com.github.benmanes.caffeine.cache.AsyncLoadingCache
import com.github.benmanes.caffeine.cache.Caffeine
import com.inepex.nyomagestreamprocessor.common.dto.DeviceToObjectMapping
import com.inepex.nyomagestreamprocessor.companyapi.GetChangedDeviceToObjectMappingSinceApi
import com.inepex.nyomagestreamprocessor.logger.Logger
import com.inepex.nyomagestreamprocessor.scheduler.Scheduler
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class DeviceToObjectMappingCache
constructor(
        scheduler: Scheduler,
        private val logger: Logger,
        private val getChangedDeviceToObjectMappingSinceApi: GetChangedDeviceToObjectMappingSinceApi) {

    /**
     * <company id, Pair<lastReloadTimestampMs, mappings>>
     */
    private var mappingsCache: AsyncLoadingCache<Long, Pair<Long, List<DeviceToObjectMapping>>> =
            Caffeine.newBuilder()
                    .executor(scheduler.get())
                    .expireAfterAccess(1, TimeUnit.HOURS)
                    .refreshAfterWrite(20, TimeUnit.SECONDS)
                    .buildAsync(object : AsyncCacheLoader<Long, Pair<Long, List<DeviceToObjectMapping>>> {
                        override fun asyncLoad(key: Long, executor: Executor): CompletableFuture<Pair<Long, List<DeviceToObjectMapping>>> {
//                            logger.info("DeviceToObjectMappingCache: asyncLoad for key: $key")
                            val now = System.currentTimeMillis()
                            return getChangedDeviceToObjectMappingSinceApi.execute(companyIdToApiUrlMap[key]!!, 0, executor)
                                    .thenApplyAsync {
//                                        logger.info("DeviceToObjectMappingCache: result for asyncLoad $it")
                                        now to it.changed
                                    }
                        }

                        override fun asyncReload(key: Long, oldValue: Pair<Long, List<DeviceToObjectMapping>>, executor: Executor): CompletableFuture<Pair<Long, List<DeviceToObjectMapping>>> {
//                            logger.info("DeviceToObjectMappingCache: asyncReload for key: $key")
                            val now = System.currentTimeMillis()
                            return getChangedDeviceToObjectMappingSinceApi.execute(
                                    companyIdToApiUrlMap[key]!!, oldValue.first, executor)
                                    .thenApplyAsync {
//                                        logger.info("DeviceToObjectMappingCache: result for asyncReload $it")
                                        now to processChangedMappings(oldValue.second, it.changed)
                                    }
                        }
                    })

    private val companyIdToApiUrlMap = ConcurrentHashMap<Long, String>()


    fun getMappingsForDeviceAtTime(companyId: Long, apiUrl: String, deviceId: Long, timestampMs: Long): CompletableFuture<List<DeviceToObjectMapping>> {
//        logger.info("DeviceToObjectMappingCache.getMappings")
        companyIdToApiUrlMap[companyId] = apiUrl
        return mappingsCache.get(companyId).thenApplyAsync {
//            logger.info("DeviceToObjectMappingCache: filtering response")
            it.second.filter { mapping ->
                mapping.deviceId == deviceId &&
                        ((mapping.endTimestamp == null && mapping.beginTimestamp <= timestampMs)
                                || (mapping.beginTimestamp <= timestampMs && mapping.endTimestamp!! > timestampMs))
            }

        }
    }

    private fun processChangedMappings(original: List<DeviceToObjectMapping>, changed: List<DeviceToObjectMapping>): List<DeviceToObjectMapping> {
        // TODO implement merge
        return original
    }
}
