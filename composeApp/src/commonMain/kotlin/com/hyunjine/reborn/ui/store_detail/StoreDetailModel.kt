package com.hyunjine.reborn.ui.store_detail

import com.hyunjine.reborn.common.util.now
import com.hyunjine.reborn.common.util.pad
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

/**
 * ?…ì²´ ?ì„¸ ?”ë©´??UI ?íƒœ ëª¨ë¸.
 * @param name ?…ì²´ëª? * @param address ?…ì²´ ì£¼ì†Œ
 * @param description ?…ì²´ ?Œê°œ ?ìŠ¤?? * @param businessHours ?ì—… ?œê°„ ëª©ë¡
 * @param prices ë§¤ì… ?œì„¸ ëª©ë¡
 * @param lastUpdated ?œì„¸ ìµœì¢… ?…ë°?´íŠ¸ ?œê°„ ?ìŠ¤?? * @param phoneNumber ?…ì²´ ?„í™”ë²ˆí˜¸
 */
data class StoreDetailModel(
    val id: Long,
    val imageUrls: ImmutableList<String> = persistentListOf(),
    val name: String = "",
    val address: String = "",
    val description: String = "",
    val businessHours: ImmutableList<OperationTimeModel> = persistentListOf(),
    val prices: ImmutableList<StorePriceModel> = persistentListOf(),
    val lastUpdated: LocalDateTime = LocalDateTime.now(),
    val phoneNumber: String = ""
)

/**
 * ?ì—… ?œê°„ ëª¨ë¸.
 * @param dayOfWeek ?”ì¼ëª? * @param operation ?ì—… ?œê°„ ?ìŠ¤?? */
data class OperationTimeModel(
    val dayOfWeek: DayOfWeek,
    val operation: Operation
)

sealed interface Operation {
    data object Closed: Operation {
        override fun toString(): String {
            return "?´ë¬´"
        }
    }

    data class Open(
        val start: LocalTime,
        val end: LocalTime,
    ) : Operation {
        override fun toString(): String {
            val startStr = "${start.hour.pad()}:${start.minute.pad()}"
            val endStr = "${end.hour.pad()}:${end.minute.pad()}"

            return "$startStr ~ $endStr"
        }
    }
}

/**
 * ?…ì²´ ë§¤ì… ?œì„¸ ëª¨ë¸.
 * @param name ?ˆëª©ëª? * @param price ?¨ê? ?ìŠ¤?? */
data class StorePriceModel(
    val name: String,
    val price: String
)