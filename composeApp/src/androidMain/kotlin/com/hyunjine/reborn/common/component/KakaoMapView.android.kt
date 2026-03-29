package com.hyunjine.reborn.common.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraAnimation
import com.kakao.vectormap.camera.CameraUpdateFactory

/**
 * 카카오 지도를 표시하는 Android 구현체입니다.
 *
 * @param latitude 지도 중심의 위도입니다.
 * @param longitude 지도 중심의 경도입니다.
 * @param moveToMyLocation 이 값이 변경되면 현재 위치로 카메라를 이동합니다.
 * @param modifier Modifier입니다.
 */
@Composable
actual fun KakaoMapView(
    latitude: Double,
    longitude: Double,
    moveToMyLocation: Int,
    modifier: Modifier
) {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    val kakaoMapState = remember { mutableStateOf<KakaoMap?>(null) }

    DisposableEffect(Unit) {
        mapView.start(
            object : MapLifeCycleCallback() {
                override fun onMapDestroy() = Unit
                override fun onMapError(error: Exception) = Unit
            },
            object : KakaoMapReadyCallback() {
                override fun onMapReady(kakaoMap: KakaoMap) {
                    kakaoMapState.value = kakaoMap
                    val position = LatLng.from(latitude, longitude)
                    kakaoMap.moveCamera(CameraUpdateFactory.newCenterPosition(position))
                }
            }
        )
        onDispose {
            mapView.finish()
        }
    }

    LaunchedEffect(moveToMyLocation) {
        if (moveToMyLocation > 0) {
            kakaoMapState.value?.moveCamera(
                CameraUpdateFactory.newCenterPosition(LatLng.from(latitude, longitude)),
                CameraAnimation.from(300)
            )
        }
    }

    AndroidView(
        factory = { mapView },
        modifier = modifier
    )
}
