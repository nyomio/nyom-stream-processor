package com.inepex.nyomagestreamprocessor.pipeline

import com.inepex.nyomagestreamprocessor.companyapi.GetChangedDeviceToObjectMappingSinceApi
import com.inepex.nyomagestreamprocessor.config.Configuration
import com.inepex.nyomagestreamprocessor.config.ConfigurationBuilderService
import com.inepex.nyomagestreamprocessor.deviceinfo.DeviceInfoCache
import com.inepex.nyomagestreamprocessor.devicemanagerapi.DeviceManagerApi
import com.inepex.nyomagestreamprocessor.devicetoobjectmappingcache.DeviceToObjectMappingCache
import com.inepex.nyomagestreamprocessor.elastic.SetSecureConnectionService
import com.inepex.nyomagestreamprocessor.httpclient.HttpClient
import com.inepex.nyomagestreamprocessor.logger.Logger
import com.inepex.nyomagestreamprocessor.nyompipeline.mapincomingnyom.InterpolateLocationService
import com.inepex.nyomagestreamprocessor.nyompipeline.mapincomingnyom.MapIncomingNyomService
import com.inepex.nyomagestreamprocessor.scheduler.Scheduler

class Dependencies {

    companion object {
        private var instance: Dependencies? = null

        private var predefinedConfiguration : Configuration? = null

        @Synchronized
        fun get() : Dependencies {
            if (instance == null) {
                init()
            }
            return instance!!
        }

        fun initWithConfig(configuration: Configuration) {
            predefinedConfiguration = configuration
        }

        private fun init() {
            instance = Dependencies().apply {
                logger = Logger()
                config = predefinedConfiguration
                        ?: ConfigurationBuilderService.build(null)
                scheduler = Scheduler()
                httpClient = HttpClient()
                deviceManagerApi = DeviceManagerApi(scheduler, httpClient, logger, config)
                deviceInfoCache = DeviceInfoCache(scheduler, logger, deviceManagerApi)
                getChangedDeviceToObjectMappingSinceApi = GetChangedDeviceToObjectMappingSinceApi(scheduler, httpClient, logger, config)
                deviceToObjectMappingCache = DeviceToObjectMappingCache(scheduler, logger, getChangedDeviceToObjectMappingSinceApi)
                elasticSetSecureConnectionService = SetSecureConnectionService(config)
                mapIncomingNyomService = MapIncomingNyomService()
                interpolateLocationService = InterpolateLocationService()
            }
        }

    }

    lateinit var logger: Logger
    lateinit var config : Configuration
    lateinit var scheduler: Scheduler
    lateinit var httpClient: HttpClient
    lateinit var deviceManagerApi: DeviceManagerApi
    lateinit var deviceInfoCache : DeviceInfoCache
    lateinit var getChangedDeviceToObjectMappingSinceApi: GetChangedDeviceToObjectMappingSinceApi
    lateinit var deviceToObjectMappingCache: DeviceToObjectMappingCache
    lateinit var elasticSetSecureConnectionService: SetSecureConnectionService
    lateinit var mapIncomingNyomService: MapIncomingNyomService
    lateinit var interpolateLocationService: InterpolateLocationService


}
