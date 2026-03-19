package com.hyunjine.reborn.entity

import kotlinx.serialization.Serializable

@Serializable
data class StoreEntity(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val distance: Int,
    val prices: List<MatterEntity>
)
