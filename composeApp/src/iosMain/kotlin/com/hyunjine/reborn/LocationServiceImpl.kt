package com.hyunjine.reborn

import com.hyunjine.reborn.data.Location
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.koin.core.annotation.Single
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.Foundation.NSError
import platform.darwin.NSObject

@Single
class LocationServiceImpl : LocationService {
    private val locationManager = CLLocationManager()

    @OptIn(ExperimentalForeignApi::class)
    override fun getLocationFlow(): Flow<Location> = callbackFlow {
        val status = CLLocationManager.authorizationStatus()
        if (status != kCLAuthorizationStatusAuthorizedWhenInUse &&
            status != kCLAuthorizationStatusAuthorizedAlways
        ) {
            close()
            return@callbackFlow
        }

        locationManager.location?.let { clLocation ->
            val (latitude, longitude) = clLocation.coordinate.useContents { latitude to longitude }
            trySend(Location(latitude, longitude))
        }

        val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {
            override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
                val clLocation = didUpdateLocations.lastOrNull() as? CLLocation ?: return
                val (latitude, longitude) = clLocation.coordinate.useContents { latitude to longitude }
                trySend(Location(latitude, longitude))
            }

            override fun locationManager(manager: CLLocationManager, didFailWithError: NSError) = Unit
        }

        locationManager.delegate = delegate
        locationManager.desiredAccuracy = platform.CoreLocation.kCLLocationAccuracyBest
        locationManager.distanceFilter = 50.0
        locationManager.startUpdatingLocation()

        awaitClose {
            locationManager.stopUpdatingLocation()
            locationManager.delegate = null
        }
    }
}
