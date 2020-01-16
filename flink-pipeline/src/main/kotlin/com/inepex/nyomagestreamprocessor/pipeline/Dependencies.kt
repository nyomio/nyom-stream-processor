package com.inepex.nyomagestreamprocessor.pipeline

import com.inepex.nyomagestreamprocessor.companyapi.GetChangedDeviceToObjectMappingSinceApi
import com.inepex.nyomagestreamprocessor.config.Configuration
import com.inepex.nyomagestreamprocessor.config.ConfigurationBuilderService
import com.inepex.nyomagestreamprocessor.nyompipeline.querydeviceinfo.DeviceInfoCache
import com.inepex.nyomagestreamprocessor.devicetoobjectmappingcache.DeviceToObjectMappingCache
import com.inepex.nyomagestreamprocessor.elastic.SetSecureConnectionService
import com.inepex.nyomagestreamprocessor.httpclient.HttpClient
import com.inepex.nyomagestreamprocessor.jwt.SystemUserJwtTokenGenerator
import com.inepex.nyomagestreamprocessor.logger.Logger
import com.inepex.nyomagestreamprocessor.nyompipeline.mapincomingnyom.InterpolateLocationService
import com.inepex.nyomagestreamprocessor.nyompipeline.mapincomingnyom.MapIncomingNyomService
import com.inepex.nyomagestreamprocessor.nyompipeline.querydeviceinfo.GetDeviceInfoHttpClient
import com.inepex.nyomagestreamprocessor.scheduler.Scheduler
import io.micronaut.context.ApplicationContext
import io.micronaut.security.token.jwt.generator.JwtTokenGenerator

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
                        ?: ConfigurationBuilderService.build(false, null)
                scheduler = Scheduler()
                httpClient = HttpClient()
                getChangedDeviceToObjectMappingSinceApi = GetChangedDeviceToObjectMappingSinceApi(scheduler, httpClient, logger, config)
                deviceToObjectMappingCache = DeviceToObjectMappingCache(scheduler, logger, getChangedDeviceToObjectMappingSinceApi)
                elasticSetSecureConnectionService = SetSecureConnectionService(config)
                mapIncomingNyomService = MapIncomingNyomService()
                interpolateLocationService = InterpolateLocationService()
                micronautContext = ApplicationContext.run()
                getDeviceInfoHttpClient = micronautContext.getBean(GetDeviceInfoHttpClient::class.java)
                jwtTokenGenerator = SystemUserJwtTokenGenerator(micronautContext.getBean(JwtTokenGenerator::class.java))
                deviceInfoCache = DeviceInfoCache(scheduler, logger, getDeviceInfoHttpClient, jwtTokenGenerator)
            }
        }
    }

    lateinit var logger: Logger
    lateinit var config : Configuration
    lateinit var scheduler: Scheduler
    lateinit var httpClient: HttpClient
    lateinit var deviceInfoCache : DeviceInfoCache
    lateinit var getChangedDeviceToObjectMappingSinceApi: GetChangedDeviceToObjectMappingSinceApi
    lateinit var deviceToObjectMappingCache: DeviceToObjectMappingCache
    lateinit var elasticSetSecureConnectionService: SetSecureConnectionService
    lateinit var mapIncomingNyomService: MapIncomingNyomService
    lateinit var interpolateLocationService: InterpolateLocationService
    lateinit var micronautContext: ApplicationContext
    lateinit var getDeviceInfoHttpClient: GetDeviceInfoHttpClient
    lateinit var jwtTokenGenerator: SystemUserJwtTokenGenerator


}
