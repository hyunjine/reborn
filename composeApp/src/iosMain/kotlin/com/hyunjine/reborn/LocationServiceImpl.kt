package com.hyunjine.reborn

import com.hyunjine.reborn.data.Location
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import org.koin.core.annotation.Single
import platform.CoreLocation.CLGeocoder
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.CLPlacemark
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.Foundation.NSError
import platform.darwin.NSObject
import kotlin.coroutines.resume

@Single
class LocationServiceImpl : LocationService {
    private val locationManager = CLLocationManager()
    private val geocoder = CLGeocoder()

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
            val address = reverseGeocode(clLocation)
            trySend(Location(latitude, longitude, address))
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

    private suspend fun reverseGeocode(location: CLLocation): String {
        return try {
            suspendCancellableCoroutine { cont ->
                geocoder.reverseGeocodeLocation(location) { placemarks, _ ->
                    val placemark = (placemarks?.firstOrNull() as? CLPlacemark)
                    val sido = placemark?.administrativeArea ?: ""
                    val sigungu = placemark?.subAdministrativeArea ?: placemark?.locality ?: ""
                    cont.resume("$sido $sigungu".trim())
                }
            }
        } catch (e: Exception) {
            ""
        }
    }
}
