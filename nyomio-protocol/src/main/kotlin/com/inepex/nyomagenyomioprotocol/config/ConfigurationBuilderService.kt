package com.inepex.nyomagenyomioprotocol.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties

class ConfigurationBuilderService {

    companion object {
        fun build(runningInIDEWithProfile: String?): Configuration {
            return if (runningInIDEWithProfile != null) parseYaml(runningInIDEWithProfile)
            else parseEnvironmentVariables()
        }

        private fun parseYaml(profile:String) : Configuration {
            try {
                return ObjectMapper(YAMLFactory()).readValue(this::class.java.classLoader
                        .getResourceAsStream("config/$profile.yaml"), Configuration::class.java)
            } catch (error: Throwable) {
                throw RuntimeException("Couldn't read config file: \"config/$profile.yaml\"", error)
            }
        }

        private fun parseEnvironmentVariables() : Configuration {
            return Configuration().apply {
                Configuration::class.declaredMemberProperties.forEach {
                    val tmp = System.getenv(it.name)
                    if (tmp != null) {
                        it as KMutableProperty<*>
                        it.setter.call(this, tmp)
                    }
                }
            }
        }
    }


}
