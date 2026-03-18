package com.hyunjine.reborn.ui.home

import kotlinx.collections.immutable.ImmutableList
import kotlin.jvm.JvmInline
import androidx.compose.runtime.Stable
import kotlin.math.roundToInt

@Stable
sealed interface StoreState {
    data object Loading: StoreState

    /**
     * ???”ë©´??UI ?íƒœ ëª¨ë¸.
     * @param stores ê³ ë¬¼??ëª©ë¡
     */
    data class Loaded(
        val stores: ImmutableList<StoreModel>
    ): StoreState
}

/**
 * ê³ ë¬¼???•ë³´ ëª¨ë¸.
 * @param id ê³ ë¬¼??ID
 * @param name ê³ ë¬¼???´ë¦„
 * @param imageUrl ê³ ë¬¼???´ë?ì§€ URL
 * @param distance ?„ì¬ ?„ì¹˜ë¡œë??°ì˜ ê±°ë¦¬
 * @param prices ë§¤ì… ?œì„¸ ëª©ë¡
 */
data class StoreModel(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val distance: Distance,
    val prices: ImmutableList<MatterModel>
)

/**
 * ë§¤ì… ?œì„¸ ëª¨ë¸.
 * @param name ?ˆëª©ëª?
 * @param price ?¨ê?
 */
data class MatterModel(
    val name: String,
    val price: Int
)

@Stable
@JvmInline
value class Distance private constructor(private val _meters: Int) {

    companion object {
        /** ë¯¸í„° ?¨ìœ„ë¡?Distance ê°ì²´ ?ì„± */
        fun meters(meters: Int): Distance = Distance(meters)

        /** ?¬ë¡œë¯¸í„° ?¨ìœ„ë¡?Distance ê°ì²´ ?ì„± */
        fun kilometers(km: Int): Distance = Distance(km * 1000)
    }

    // ?¨ìœ„ ë³€???„ë¡œ?¼í‹°
    val meters: Int get() = _meters
    val kilometers: Int get() = meters / 1000

    // value class?ì„œ toString???¤ë²„?¼ì´?œí•˜ë©?display()?€ ?¼ì¹˜?œì¼œ ?¼ì„ ??ë°©ì??????ˆìŠµ?ˆë‹¤.
    override fun toString(): String {
        val roundedKm = (meters / 100.0).roundToInt() / 10.0
        return "${roundedKm}km"
    }
}
