package io.github.soat7.myburgercontrol.testbdd.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.oshai.kotlinlogging.KotlinLogging
import java.util.Properties

private val log = KotlinLogging.logger { }

class Configuration {

    private val settings: Properties

    init {
        settings = loadProperties()

        log.info {
            """
            Properties:
            $settings
            """.trimIndent()
        }
    }

    private fun loadProperties(): Properties {
        val om = ObjectMapper(YAMLFactory()).registerModule(KotlinModule())
        val resource = Configuration::class.java.getResource("/application.yaml")
            ?: Configuration::class.java.getResource("/application.yml")

        if (resource == null) {
            log.warn { "Resource not found" }
            return Properties()
        }

        log.info { "Resource: $resource" }

        val map = om.readValue<Map<String, Any>>(resource)
        return convertToProperties(map)
    }

    private fun convertToProperties(map: Map<String, Any>) = run {
        val r = LinkedHashMap<String, String>()
        val prop = Properties(System.getProperties())

        System.getenv().forEach {
            prop.setProperty(it.key, it.value)
        }

        flatterMap(map, r, "")
        r.forEach {
            prop.setProperty(it.key, it.value)
        }
        prop
    }

    @Suppress("UNCHECKED_CAST")
    private fun flatterMap(inputMap: Map<String, Any>, outputMap: MutableMap<String, String>, path: String) {
        inputMap.forEach { (k, v) ->
            var key = k
            if (path.isNotBlank()) {
                key = path + (if (key.startsWith("[")) key else ".$key")
            }

            when {
                v is String -> outputMap[key] = v
                v is Map<*, *> -> flatterMap(v as Map<String, Any>, outputMap, key)
                v is Collection<*> -> {
                    v.forEachIndexed { index, element ->
                        flatterMap(mapOf("[$index]" to element!!), outputMap, key)
                    }
                }

                else -> outputMap[key] = "$v"
            }
        }
    }

    operator fun get(key: String): String? = settings.getProperty(key)?.let { value ->
        val groupValues = IS_PROPERTY_RE.matchEntire(value)?.groupValues?.get(1)
        log.info { "RE Match = " }
        groupValues?.let {
            get(it)
        } ?: value
    }

    companion object {
        private val IS_PROPERTY_RE = Regex("^\\$\\{(.+)\\}$")
    }

}