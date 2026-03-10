package com.hyunjine.reborn

import org.koin.core.annotation.Single

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

fun interface LocationService {
    suspend fun getCurrentLocation(): Location?
}

data class Location(val latitude: Double, val longitude: Double)

@Single
class LocationServiceImpl: LocationService {
    override suspend fun getCurrentLocation(): Location? {
        return Location(2.0, 1.0)
    }
}