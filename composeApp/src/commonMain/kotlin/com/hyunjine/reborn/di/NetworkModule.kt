package com.hyunjine.reborn.di

import com.hyunjine.reborn.util.immutableListSerializerModule
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class NetworkModule {

    @Single
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        serializersModule = immutableListSerializerModule
    }

    @Single
    fun provideHttpClient(json: Json): HttpClient = HttpClient {
        defaultRequest {
            url("http://192.168.1.7:8080/")
            contentType(ContentType.Application.Json)
        }
        install(ContentNegotiation) {
            json(json, contentType = ContentType.Any)
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Napier.d(message, tag = "HTTP")
                }
            }
            level = LogLevel.ALL
        }
    }
}
