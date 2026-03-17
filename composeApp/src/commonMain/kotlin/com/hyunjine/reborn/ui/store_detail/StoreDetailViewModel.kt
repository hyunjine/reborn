package com.hyunjine.reborn.ui.store_detail

import com.hyunjine.reborn.common.util.BaseViewModel
import com.hyunjine.reborn.data.store.StoreRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class StoreDetailViewModel(
    storeId: Long,
    private val repository: StoreRepository
) : BaseViewModel<StoreDetailScreen.UiEvent>() {

    val model: StateFlow<StoreDetailModel> = flow {
        emit(repository.getStoreDetail(storeId))
    }.stateIn(StoreDetailModel(id = 0L))
}
