package com.hyunjine.reborn.common.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

/**
 * iOS 위치 권한 요청.
 * @param onResult 권한 허용 여부 콜백
 */
@Composable
actual fun RequestLocationPermission(onResult: (Boolean) -> Unit) {
    // TODO: CLLocationManager를 사용한 iOS 위치 권한 요청 구현
    LaunchedEffect(Unit) {
        onResult(false)
    }
}
