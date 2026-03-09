package com.hyunjine.reborn.ui.home

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class HomeModel(
    val location: String = "강남구 역삼동",
    val filters: ImmutableList<String> = persistentListOf("전체", "고철", "비철", "폐지", "기타"),
    val selectedFilter: String = "전체",
    val centers: ImmutableList<GarbageCenterModel> = persistentListOf()
)

data class GarbageCenterModel(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val distance: String,
    val prices: ImmutableList<PriceModel>
)

data class PriceModel(
    val name: String,
    val price: String
)
