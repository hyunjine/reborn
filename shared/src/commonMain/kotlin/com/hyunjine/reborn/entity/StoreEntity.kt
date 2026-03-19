package com.hyunjine.reborn.entity

import kotlinx.serialization.Serializable

@Serializable
data class StoreEntity(
    val id: Long,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
)
