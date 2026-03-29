package com.hyunjine.reborn.common.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hyunjine.reborn.data.Location

/**
 * 카카오 지도를 표시하는 Composable입니다.
 * 플랫폼별로 다른 구현체를 사용합니다.
 *
 * @param location 지도 중심의 위치입니다.
 * @param moveToMyLocation 이 값이 변경되면 현재 위치로 카메라를 이동합니다.
 * @param modifier Modifier입니다.
 */
@Composable
expect fun KakaoMapView(
    location: Location,
    moveToMyLocation: Int = 0,
    modifier: Modifier = Modifier
)
