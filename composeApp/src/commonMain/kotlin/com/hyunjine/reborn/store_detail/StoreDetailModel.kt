package com.hyunjine.reborn.store_detail

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class StoreDetailModel(
    val name: String = "",
    val isVerified: Boolean = false,
    val address: String = "",
    val description: String = "",
    val businessHours: ImmutableList<BusinessHourModel> = persistentListOf(),
    val prices: ImmutableList<StorePriceModel> = persistentListOf(),
    val lastUpdated: String = "",
    val phoneNumber: String = ""
)

data class BusinessHourModel(
    val day: String,
    val hours: String
)

data class StorePriceModel(
    val name: String,
    val price: String
)
