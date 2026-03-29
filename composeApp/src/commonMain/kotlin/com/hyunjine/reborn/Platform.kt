package com.hyunjine.reborn

import com.hyunjine.reborn.data.Location
import kotlinx.coroutines.flow.Flow

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

interface LocationService {
    fun getLocationFlow(): Flow<Location>
}