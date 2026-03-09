package com.hyunjine.reborn.ui.regist_store

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class RegistStoreModel(
    val storeName: String = "",
    val address: String = "",
    val description: String = "",
    val photoCount: Int = 0,
    val maxPhotoCount: Int = 5,
    val batchStartTime: String = "",
    val batchEndTime: String = "",
    val daySchedules: ImmutableList<DayScheduleModel> = persistentListOf(
        DayScheduleModel("월", true),
        DayScheduleModel("화", true),
        DayScheduleModel("수", true),
        DayScheduleModel("목", true),
        DayScheduleModel("금", true),
        DayScheduleModel("토", true),
        DayScheduleModel("일", true)
    ),
    val isHolidayClosed: Boolean = false,
    val priceItems: ImmutableList<PriceItemModel> = persistentListOf()
)

data class DayScheduleModel(
    val day: String,
    val isEnabled: Boolean = true,
    val startTime: String = "",
    val endTime: String = ""
)

data class PriceItemModel(
    val name: String = "",
    val price: String = ""
)
