package com.hyunjine.reborn.ui.store_detail

import com.hyunjine.reborn.common.util.BaseViewModel
import com.hyunjine.reborn.common.util.now
import com.hyunjine.reborn.data.store.StoreRepository
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class StoreDetailViewModel(
    storeId: Long,
    private val repository: StoreRepository
) : BaseViewModel<StoreDetailScreen.UiEvent>() {

    val model: StateFlow<StoreDetailModel> = flow {
        emit(repository.getStoreDetail(storeId))
    }.stateIn(StoreDetailModel(
        id = storeId,
        imageUrls = persistentListOf(),
        name = "",
        address = "",
        description = "",
        businessHours = DayOfWeek.entries.map {
            OperationTimeModel(
                dayOfWeek = it,
                operation = Operation.Open(start = LocalTime(0, 0), end = LocalTime(0, 0))
            )
        }.toImmutableList(),
        prices = persistentListOf(),
        lastUpdated = LocalDateTime.now(),
        phoneNumber = ""
    ))
}
