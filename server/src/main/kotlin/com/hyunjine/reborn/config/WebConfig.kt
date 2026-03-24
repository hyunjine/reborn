package com.hyunjine.reborn.config

import kotlinx.serialization.json.Json
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.http.codec.json.KotlinSerializationJsonDecoder

import org.springframework.web.reactive.config.WebFluxConfigurer

@Configuration
class WebConfig : WebFluxConfigurer {

    @Bean
    fun json(): Json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    override fun configureHttpMessageCodecs(configurer: ServerCodecConfigurer) {
        val json = json()
        configurer.defaultCodecs().kotlinSerializationJsonDecoder(KotlinSerializationJsonDecoder(json))
        configurer.defaultCodecs().kotlinSerializationJsonEncoder(PolymorphicKotlinSerializationEncoder(json))
    }
}
