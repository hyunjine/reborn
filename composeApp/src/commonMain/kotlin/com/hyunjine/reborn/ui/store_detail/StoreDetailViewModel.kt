package com.hyunjine.reborn.ui.store_detail

import androidx.lifecycle.viewModelScope
import com.hyunjine.reborn.common.util.BaseViewModel
import com.hyunjine.reborn.data.store.StoreRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class StoreDetailViewModel(
    storeId: Long,
    private val repository: StoreRepository
) : BaseViewModel() {
    private val uiEvent = MutableSharedFlow<StoreDetailScreen.UiEvent>()

    val model: StateFlow<StoreDetailModel> = flow {
        emit(repository.getStoreDetail(storeId))
    }.stateIn(StoreDetailModel(id = 0L))

    fun event(event: StoreDetailScreen.UiEvent) {
        viewModelScope.launch {
            uiEvent.emit(event)
        }
    }
}
