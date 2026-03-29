package com.hyunjine.reborn.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.hyunjine.reborn.data.Location

/**
 * 카카오 지도를 표시하는 iOS 구현체입니다.
 * 현재는 플레이스홀더로 구현되어 있습니다.
 *
 * @param location 지도 중심의 위치입니다.
 * @param moveToMyLocation 이 값이 변경되면 현재 위치로 카메라를 이동합니다.
 * @param modifier Modifier입니다.
 */
@Composable
actual fun KakaoMapView(
    location: Location,
    moveToMyLocation: Int,
    modifier: Modifier
) {
    Box(
        modifier = modifier.background(Color(0xFFE8E8E8)),
        contentAlignment = Alignment.Center
    ) {
        Text("지도 (iOS 미지원)")
    }
}
