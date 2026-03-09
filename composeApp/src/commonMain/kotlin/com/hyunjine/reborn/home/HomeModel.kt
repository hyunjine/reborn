package com.hyunjine.reborn.home

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class HomeModel(
    val location: String = "개포동",
    val filters: ImmutableList<String> = persistentListOf("전체", "고철", "비철", "알루미늄", "스텐"),
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
