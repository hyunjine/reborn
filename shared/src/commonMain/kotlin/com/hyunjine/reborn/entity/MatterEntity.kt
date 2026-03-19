package com.hyunjine.reborn.data.store.entity

import kotlinx.serialization.Serializable

@Serializable
data class MatterEntity(
    val name: String,
    val price: Int
)