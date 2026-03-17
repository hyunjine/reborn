package com.hyunjine.reborn.ui.regist_store

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import com.hyunjine.reborn.common.util.now
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentHashMap
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime
import kotlin.jvm.JvmInline

/**
 * 업체 등록 화면의 UI 상태 모델.
 * @param name 업체명
 * @param phone 전화번호 (숫자만)
 * @param address 업체 주소
 * @param description 업체 소개
 * @param photos 등록된 사진 ByteArray 목록
 * @param batchStartTime 일괄 적용 시작 시간
 * @param batchEndTime 일괄 적용 종료 시간
 * @param daySchedules 요일별 영업 시간 목록
 * @param priceItems 매입 단가 항목 목록
 */
@Stable
data class RegistStoreModel(
    val name: String = "",
    val phone: String = "010",
    val address: String = "",
    val description: String = "",
    val photos: ImmutableList<ByteArray> = persistentListOf(),
    val batchStartTime: LocalTime = LocalTime(0, 0),
    val batchEndTime: LocalTime = LocalTime(0, 0),
    val daySchedules: PersistentMap<DayOfWeek, DayScheduleModel> = DayOfWeek.entries
        .associateWith { DayScheduleModel() }
        .toPersistentHashMap(),
    val priceItems: ImmutableList<PriceItemModel> = persistentListOf(PriceItemModel()),
) {
    fun isValid(): String? {
        if (name.isBlank()) return "업체명을 입력해주세요."
        if (address.isBlank()) return "주소를 입력해주세요."
        if (phone.isBlank()) return "전화번호를 입력해주세요."

        // 2. 영업 시간 검증 (둘 다 00:00이면 24시간 운영)
        val is24HourBatch = batchStartTime == LocalTime(0, 0) && batchEndTime == LocalTime(0, 0)
        if (!is24HourBatch && batchStartTime >= batchEndTime) {
            return "영업 종료 시각은 시작 시각보다 늦어야 합니다."
        }

        // 3. 사진 검증
        if (photos.isEmpty()) {
            return "가게 사진을 최소 1장 이상 등록해주세요."
        }

        // 4. 요일별 스케줄 검증
        // DayScheduleModel.isValid()가 Boolean을 반환한다고 가정 시
        val hasInvalidSchedule = daySchedules.values.any { schedule ->
            if (!schedule.isEnabled) return@any false
            val is24Hour = schedule.startTime == LocalTime(0, 0) && schedule.endTime == LocalTime(0, 0)
            !is24Hour && schedule.startTime >= schedule.endTime
        }
        if (hasInvalidSchedule) {
            return "요일별 상세 일정을 확인해주세요."
        }

        // 5. 가격 아이템 검증
        if (priceItems.isEmpty()) {
            return "매입 단가를 최소 하나 이상 등록해주세요."
        }

        // 개별 아이템 검증 (예: 메뉴 이름이나 가격이 비었는지)
        if (priceItems.any { !it.isValid() }) {
            return "등록된 매입 단가 정보가 올바르지 않습니다."
        }

        return null
    }
}

/**
 * 요일별 영업 시간 모델.
 * @param isEnabled 해당 요일 영업 여부
 * @param startTime 영업 시작 시간
 * @param endTime 영업 종료 시간
 */
data class DayScheduleModel(
    val isEnabled: Boolean = true,
    val startTime: LocalTime = LocalTime(0, 0),
    val endTime: LocalTime = LocalTime(0, 0)
)

/**
 * 매입 단가 항목 모델.
 * @param name 품목명
 * @param price 단가 텍스트
 */
@Stable
data class PriceItemModel(
    val name: ItemName = ItemName.None,
    val price: Int? = null
) {
    fun isValid(): Boolean {
        // 1. 이름 검증: None이 아니어야 하며, 내용은 공백이 아니어야 함
        val isNameValid = when (name) {
            is ItemName.None -> false
            is ItemName.Basic -> name.value.isNotBlank()
            is ItemName.Custom -> name.value.isNotBlank()
        }

        // 2. 가격 검증
        val isPriceValid = price != null

        return isNameValid && isPriceValid
    }
}

sealed interface ItemName {
    val value: String
    data object None: ItemName {
        override val value: String = "품목을 선택하세요."
    }

    @JvmInline
    value class Basic(override val value: String): ItemName

    @JvmInline
    value class Custom(override val value: String): ItemName
}
