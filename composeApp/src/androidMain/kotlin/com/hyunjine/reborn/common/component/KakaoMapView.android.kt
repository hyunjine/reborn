package com.hyunjine.reborn.common.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.hyunjine.reborn.R
import com.hyunjine.reborn.data.Location
import com.hyunjine.reborn.util.log
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraAnimation
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles

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
    location: Location,
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
                    val position = LatLng.from(location.latitude, location.longitude)
                    kakaoMap.moveCamera(CameraUpdateFactory.newCenterPosition(position))

                    val labelStyles = LabelStyles.from(LabelStyle.from(R.drawable.my_location))
                    val labelOptions = LabelOptions.from("my_location", position)
                        .setStyles(labelStyles)
                    kakaoMap.labelManager?.layer?.addLabel(labelOptions)
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
                CameraUpdateFactory.newCenterPosition(LatLng.from(location.latitude, location.longitude)),
                CameraAnimation.from(300)
            )
        }
    }

    AndroidView(
        factory = { mapView },
        modifier = modifier
    )
}
