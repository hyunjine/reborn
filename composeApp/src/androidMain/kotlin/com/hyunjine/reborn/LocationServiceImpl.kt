package com.hyunjine.reborn

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.hyunjine.reborn.data.Location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Single
class LocationServiceImpl : LocationService {
    private val context get() = RebornApplication.appContext
    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    override suspend fun getCurrentLocation(): Location? {
        val hasPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasPermission) return null

        return try {
            val location = fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                CancellationTokenSource().token
            ).await() ?: return null

            val address = reverseGeocode(location.latitude, location.longitude)
            Location(location.latitude, location.longitude, address)
        } catch (e: Exception) {
            null
        }
    }

    private suspend fun reverseGeocode(latitude: Double, longitude: Double): String {
        if (!Geocoder.isPresent()) return ""
        val geocoder = Geocoder(context)

        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                suspendCoroutine { cont ->
                    geocoder.getFromLocation(latitude, longitude, 1) { addresses ->
                        cont.resume(addresses.firstOrNull()?.let { formatAddress(it) } ?: "")
                    }
                }
            } else {
                withContext(Dispatchers.IO) {
                    @Suppress("DEPRECATION")
                    val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                    addresses?.firstOrNull()?.let { formatAddress(it) } ?: ""
                }
            }
        } catch (e: Exception) {
            ""
        }
    }

    private fun formatAddress(address: android.location.Address): String {
        val sido = address.adminArea ?: ""
        val sigungu = address.subAdminArea ?: address.locality ?: ""
        return "$sido $sigungu".trim()
    }
}
