package com.hyunjine.reborn.ui.home

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * 홈 화면의 UI 상태 모델.
 * @param location 현재 위치 텍스트
 * @param filters 필터 목록
 * @param selectedFilter 현재 선택된 필터
 * @param centers 고물상 목록
 */
data class HomeModel(
    val location: String,
    val filters: ImmutableList<String>,
    val selectedFilter: String,
    val centers: ImmutableList<GarbageCenterModel>
)

/**
 * 고물상 정보 모델.
 * @param id 고물상 ID
 * @param name 고물상 이름
 * @param imageUrl 고물상 이미지 URL
 * @param distance 현재 위치로부터의 거리
 * @param prices 매입 시세 목록
 */
data class GarbageCenterModel(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val distance: String,
    val prices: ImmutableList<PriceModel>
)

/**
 * 매입 시세 모델.
 * @param name 품목명
 * @param price 단가 텍스트
 */
data class PriceModel(
    val name: String,
    val price: String
)
