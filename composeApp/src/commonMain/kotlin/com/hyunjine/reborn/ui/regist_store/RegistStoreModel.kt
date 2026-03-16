package com.hyunjine.reborn.ui.regist_store

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * 업체 등록 화면의 UI 상태 모델.
 * @param name 업체명
 * @param phone 전화번호 (숫자만)
 * @param address 업체 주소
 * @param description 업체 소개
 * @param photos 등록된 사진 ByteArray 목록
 * @param maxPhotoCount 최대 사진 수
 * @param batchStartTime 일괄 적용 시작 시간
 * @param batchEndTime 일괄 적용 종료 시간
 * @param daySchedules 요일별 영업 시간 목록
 * @param priceItems 매입 단가 항목 목록
 */
data class RegistStoreModel(
    val name: String = "",
    val phone: String = "010",
    val address: String = "",
    val description: String = "",
    val photos: ImmutableList<ByteArray> = persistentListOf(),
    val maxPhotoCount: Int = 5,
    val batchStartTime: String = "",
    val batchEndTime: String = "",
    val daySchedules: ImmutableList<DayScheduleModel> = persistentListOf(
        DayScheduleModel("월", true),
        DayScheduleModel("화", true),
        DayScheduleModel("수", true),
        DayScheduleModel("목", true),
        DayScheduleModel("금", true),
        DayScheduleModel("토", true),
        DayScheduleModel("일", true)
    ),
    val priceItems: ImmutableList<PriceItemModel> = persistentListOf(PriceItemModel()),
    val isShowingAddressSearch: Boolean = false
)

/**
 * 요일별 영업 시간 모델.
 * @param day 요일 약칭 (월, 화, 수 등)
 * @param isEnabled 해당 요일 영업 여부
 * @param startTime 영업 시작 시간
 * @param endTime 영업 종료 시간
 */
data class DayScheduleModel(
    val day: String,
    val isEnabled: Boolean = true,
    val startTime: String = "",
    val endTime: String = ""
)

/**
 * 매입 단가 항목 모델.
 * @param name 품목명
 * @param price 단가 텍스트
 */
data class PriceItemModel(
    val name: String = "",
    val customName: String = "",
    val price: String = ""
)
