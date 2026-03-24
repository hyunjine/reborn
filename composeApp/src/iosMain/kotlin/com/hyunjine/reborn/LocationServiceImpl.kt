package com.hyunjine.reborn

import com.hyunjine.reborn.data.Location
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.suspendCancellableCoroutine
import org.koin.core.annotation.Single
import platform.CoreLocation.CLGeocoder
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLPlacemark
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import kotlin.coroutines.resume

@Single
class LocationServiceImpl : LocationService {
    private val locationManager = CLLocationManager()
    private val geocoder = CLGeocoder()

    @OptIn(ExperimentalForeignApi::class)
    override suspend fun getCurrentLocation(): Location? {
        val status = CLLocationManager.authorizationStatus()
        if (status != kCLAuthorizationStatusAuthorizedWhenInUse &&
            status != kCLAuthorizationStatusAuthorizedAlways
        ) return null

        val clLocation = locationManager.location ?: return null
        val (latitude, longitude) = clLocation.coordinate.useContents { latitude to longitude }
        val address = reverseGeocode(clLocation)
        return Location(latitude, longitude, address)
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
