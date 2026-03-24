package com.hyunjine.reborn.common.component

import androidx.compose.runtime.Composable

/**
 * 위치 권한을 요청하는 플랫폼별 Composable.
 * 화면에 진입하면 자동으로 위치 권한을 요청합니다.
 * @param onResult 권한 허용 여부 콜백
 */
@Composable
expect fun RequestLocationPermission(onResult: (Boolean) -> Unit)
