package com.hyunjine.reborn.data

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

/**
 * 위치 정보 모델.
 *
 * @param latitude 위도
 * @param longitude 경도
 */
@Serializable
@Stable
data class Location(val latitude: Double = 0.0, val longitude: Double = 0.0)
