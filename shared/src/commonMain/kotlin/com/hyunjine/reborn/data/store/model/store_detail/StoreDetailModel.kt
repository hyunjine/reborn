package com.hyunjine.reborn.data.store.model.store_detail

import androidx.compose.runtime.Stable
import com.hyunjine.reborn.util.ImmutableListSerializer
import com.hyunjine.reborn.util.SerializableImmutableList
import com.hyunjine.reborn.util.pad
import kotlinx.collections.immutable.ImmutableList
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

/**
 * 업체 상세 화면의 UI 상태 모델.
 * @param name 업체명
 * @param address 업체 주소
 * @param description 업체 소개 텍스트
 * @param businessHours 영업 시간 목록
 * @param prices 매입 시세 목록
 * @param lastUpdated 시세 최종 업데이트 시간 텍스트
 * @param phoneNumber 업체 전화번호
 */
@Serializable
@Stable
data class StoreDetailModel(
    val id: Long,
    val imageUrls: SerializableImmutableList<String>,
    val name: String,
    val address: String,
    val description: String,
    val businessHours: SerializableImmutableList<OperationTimeModel>,
    val prices: SerializableImmutableList<StorePriceModel>,
    val lastUpdated: LocalDateTime,
    val phoneNumber: String
)

/**
 * 영업 시간 모델.
 * @param dayOfWeek 요일명
 * @param operation 영업 시간 텍스트
 */
@Serializable
data class OperationTimeModel(
    val dayOfWeek: DayOfWeek,
    val operation: Operation
)

@Serializable
sealed interface Operation {
    @Serializable
    data object Closed: Operation {
        override fun toString(): String {
            return "휴무"
        }
    }

    @Serializable
    data class Open(
        val start: LocalTime,
        val end: LocalTime,
    ) : Operation {
        override fun toString(): String {
            val startStr = "${start.hour.pad()}:${start.minute.pad()}"
            val endStr = "${end.hour.pad()}:${end.minute.pad()}"

            return "$startStr - $endStr"
        }
    }
}

/**
 * 업체 매입 시세 모델.
 * @param name 품목명
 * @param price 단가 텍스트
 */
@Serializable
data class StorePriceModel(
    val name: String,
    val price: String
)