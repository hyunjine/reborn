package com.hyunjine.reborn.model

import kotlinx.serialization.Serializable

@Serializable
data class MatterEntity(
    val name: String,
    val price: Int
)