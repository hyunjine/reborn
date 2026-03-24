package com.hyunjine.reborn.common.component

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

/**
 * Android 위치 권한 요청.
 * ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION 권한을 동시에 요청합니다.
 * @param onResult 권한 허용 여부 콜백 (둘 중 하나라도 허용되면 true)
 */
@Composable
actual fun RequestLocationPermission(onResult: (Boolean) -> Unit) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.values.any { it }
        onResult(granted)
    }

    LaunchedEffect(Unit) {
        launcher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }
}
