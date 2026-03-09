package com.hyunjine.reborn.ui.home

import kotlinx.collections.immutable.ImmutableList
import kotlin.jvm.JvmInline
import androidx.compose.runtime.Stable
import kotlin.math.roundToInt

/**
 * 홈 화면의 UI 상태 모델.
 * @param location 현재 위치 텍스트
 * @param filters 필터 목록
 * @param stores 고물상 목록
 */
data class HomeModel(
    val location: String,
    val filters: ImmutableList<Filter>,
    val stores: ImmutableList<StoreModel>
) {
    data class Filter(
        val isSelected: Boolean,
        val name: String
    )
}

/**
 * 고물상 정보 모델.
 * @param id 고물상 ID
 * @param name 고물상 이름
 * @param imageUrl 고물상 이미지 URL
 * @param distance 현재 위치로부터의 거리
 * @param prices 매입 시세 목록
 */
data class StoreModel(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val distance: Distance,
    val prices: ImmutableList<MatterModel>
)

/**
 * 매입 시세 모델.
 * @param name 품목명
 * @param price 단가
 */
data class MatterModel(
    val name: String,
    val price: Int
)

@Stable
@JvmInline
value class Distance private constructor(private val _meters: Int) {

    companion object {
        /** 미터 단위로 Distance 객체 생성 */
        fun meters(meters: Int): Distance = Distance(meters)

        /** 킬로미터 단위로 Distance 객체 생성 */
        fun kilometers(km: Int): Distance = Distance(km * 1000)
    }

    // 단위 변환 프로퍼티
    val meters: Int get() = _meters
    val kilometers: Int get() = meters / 1000

    /**
     * 리스트 화면 표시용 포맷팅
     * 모든 거리를 km 단위로 소수점 한자리까지 표시
     * 예: 1200m -> 1.2km, 500m -> 0.5km
     */
    fun display(): String {
        // 100으로 나누고 반올림 후 다시 10으로 나누어 소수점 한 자리를 유지
        val roundedKm = (meters / 100.0).roundToInt() / 10.0
        return "${roundedKm}km"
    }
}
