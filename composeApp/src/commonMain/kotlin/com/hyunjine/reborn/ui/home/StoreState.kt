package com.hyunjine.reborn.ui.home

import kotlinx.collections.immutable.ImmutableList
import kotlin.jvm.JvmInline
import androidx.compose.runtime.Stable
import kotlin.math.roundToInt

@Stable
sealed interface StoreState {
    data object Loading: StoreState

    /**
     * 홈 화면의 UI 상태 모델.
     * @param stores 고물상 목록
     */
    data class Loaded(
        val stores: ImmutableList<StoreModel>
    ): StoreState
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

    // value class에서 toString을 오버라이드하면 display()와 일치시켜 혼선을 방지할 수 있습니다.
    override fun toString(): String {
        val roundedKm = (meters / 100.0).roundToInt() / 10.0
        return "${roundedKm}km"
    }
}
