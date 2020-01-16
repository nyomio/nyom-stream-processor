package com.inepex.nyomagestreamprocessor.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import org.slf4j.LoggerFactory
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.javaType

class ConfigurationBuilderService {

    companion object {
        fun build(parseEnvVars: Boolean, runningInIDEWithProfile: String?): Configuration {
            return if (!parseEnvVars) parseYaml(runningInIDEWithProfile!!)
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
                        when (it.returnType.javaType.typeName) {
                            "int" -> it.setter.call(this, tmp.toInt())
                            else -> it.setter.call(this, tmp)
                        }
                    }
                }
            }
        }
    }


}
