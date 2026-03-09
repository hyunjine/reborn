package com.hyunjine.reborn.ui.store_detail

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * 업체 상세 화면의 UI 상태 모델.
 * @param name 업체명
 * @param isVerified 인증 업체 여부
 * @param address 업체 주소
 * @param description 업체 소개 텍스트
 * @param businessHours 영업 시간 목록
 * @param prices 매입 시세 목록
 * @param lastUpdated 시세 최종 업데이트 시간 텍스트
 * @param phoneNumber 업체 전화번호
 */
data class StoreDetailModel(
    val name: String = "",
    val isVerified: Boolean = false,
    val address: String = "",
    val description: String = "",
    val businessHours: ImmutableList<BusinessHourModel> = persistentListOf(),
    val prices: ImmutableList<StorePriceModel> = persistentListOf(),
    val lastUpdated: String = "",
    val phoneNumber: String = ""
)

/**
 * 영업 시간 모델.
 * @param day 요일명
 * @param hours 영업 시간 텍스트
 */
data class BusinessHourModel(
    val day: String,
    val hours: String
)

/**
 * 업체 매입 시세 모델.
 * @param name 품목명
 * @param price 단가 텍스트
 */
data class StorePriceModel(
    val name: String,
    val price: String
)
