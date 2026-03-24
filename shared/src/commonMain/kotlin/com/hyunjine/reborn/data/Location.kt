package com.hyunjine.reborn.data

import kotlinx.serialization.Serializable

/**
 * 위치 정보 모델.
 *
 * @param latitude 위도
 * @param longitude 경도
 */
@Serializable
data class Location(val latitude: Double, val longitude: Double)
