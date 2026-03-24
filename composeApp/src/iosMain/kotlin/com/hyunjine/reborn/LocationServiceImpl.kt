package com.hyunjine.reborn

import com.hyunjine.reborn.data.Location
import org.koin.core.annotation.Single

@Single
class LocationServiceImpl : LocationService {
    override suspend fun getCurrentLocation(): Location? {
        // TODO: CLLocationManager 구현
        return null
    }
}
