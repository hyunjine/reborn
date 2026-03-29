package com.hyunjine.reborn.common.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory

/**
 * 카카오 지도를 표시하는 Android 구현체입니다.
 *
 * @param latitude 지도 중심의 위도입니다.
 * @param longitude 지도 중심의 경도입니다.
 * @param modifier Modifier입니다.
 */
@Composable
actual fun KakaoMapView(
    latitude: Double,
    longitude: Double,
    modifier: Modifier
) {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }

    DisposableEffect(Unit) {
        mapView.start(
            object : MapLifeCycleCallback() {
                override fun onMapDestroy() = Unit
                override fun onMapError(error: Exception) = Unit
            },
            object : KakaoMapReadyCallback() {
                override fun onMapReady(kakaoMap: KakaoMap) {
                    val position = LatLng.from(latitude, longitude)
                    kakaoMap.moveCamera(CameraUpdateFactory.newCenterPosition(position))
                }
            }
        )
        onDispose {
            mapView.finish()
        }
    }

    AndroidView(
        factory = { mapView },
        modifier = modifier
    )
}
