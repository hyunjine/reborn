package com.hyunjine.reborn

import com.hyunjine.reborn.data.Location

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

fun interface LocationService {
    suspend fun getCurrentLocation(): Location?
}