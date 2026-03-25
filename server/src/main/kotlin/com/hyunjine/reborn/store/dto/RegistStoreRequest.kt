package com.hyunjine.reborn.store.dto

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

/**
 * 업체 등록 요청 DTO.
 *
 * multipart 요청의 JSON 파트로 전달됩니다.
 *
 * @param name 업체명
 * @param phone 전화번호 (숫자만)
 * @param address 업체 주소
 * @param description 업체 소개
 * @param latitude 위도 (서버에서 주소 기반으로 자동 설정)
 * @param longitude 경도 (서버에서 주소 기반으로 자동 설정)
 * @param daySchedules 요일별 영업 시간 목록
 * @param priceItems 매입 단가 항목 목록
 */
@Serializable
data class RegistStoreRequest(
    val name: String,
    val phone: String,
    val address: String,
    val description: String,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val daySchedules: List<DayScheduleRequest>,
    val priceItems: List<PriceItemRequest>
)

/**
 * 요일별 영업 시간 요청 DTO.
 *
 * @param dayOfWeek 요일
 * @param isEnabled 해당 요일 영업 여부
 * @param startTime 영업 시작 시각
 * @param endTime 영업 종료 시각
 */
@Serializable
data class DayScheduleRequest(
    val dayOfWeek: DayOfWeek,
    val isEnabled: Boolean,
    val startTime: LocalTime,
    val endTime: LocalTime
)

/**
 * 매입 단가 항목 요청 DTO.
 *
 * @param name 품목명
 * @param price 단가 (원 단위)
 * @param unit 단위 (기본값: "kg")
 */
@Serializable
data class PriceItemRequest(
    val name: String,
    val price: Int,
    val unit: String = "kg"
)
