package com.hyunjine.reborn.data.store.entity

import com.hyunjine.reborn.ui.home.Distance
import com.hyunjine.reborn.ui.home.MatterModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.serialization.Serializable

@Serializable
data class StoreEntity(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val distance: Int,
    val prices: List<MatterEntity>
)
