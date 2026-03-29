package com.hyunjine.reborn.common.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * 카카오 지도를 표시하는 Composable입니다.
 * 플랫폼별로 다른 구현체를 사용합니다.
 *
 * @param latitude 지도 중심의 위도입니다.
 * @param longitude 지도 중심의 경도입니다.
 * @param modifier Modifier입니다.
 */
@Composable
expect fun KakaoMapView(
    latitude: Double,
    longitude: Double,
    modifier: Modifier = Modifier
)
