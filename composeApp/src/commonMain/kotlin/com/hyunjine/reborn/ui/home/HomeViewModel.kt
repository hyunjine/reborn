package com.hyunjine.reborn.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyunjine.reborn.Location
import com.hyunjine.reborn.LocationService
import com.hyunjine.reborn.data.store.StoreRepository
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(
    private val storeRepository: StoreRepository
) : ViewModel() {
    private val uiEvent = MutableSharedFlow<HomeScreen.UiEvent>()

    val location: StateFlow<Location?> = flow {
        emit(storeRepository.getCurrentLocation())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val state: StateFlow<StoreState> = location
        .filterNotNull()
        .map { location ->
            StoreState.Loaded(
                storeRepository.getStores(location).map { store ->
                    store.copy(
                        prices = store.prices.take(2).toImmutableList()
                    )
                }.toImmutableList()
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), StoreState.Loading)


    fun event(event: HomeScreen.UiEvent) {
        viewModelScope.launch {
            uiEvent.emit(event)
        }
    }
}
