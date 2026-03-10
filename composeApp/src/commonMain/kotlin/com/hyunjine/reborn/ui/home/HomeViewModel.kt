package com.hyunjine.reborn.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyunjine.reborn.Location
import com.hyunjine.reborn.LocationService
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(
    private val locationService: LocationService
) : ViewModel() {
    private val uiEvent = MutableSharedFlow<HomeScreen.UiEvent>()

    val location: StateFlow<Location?> = flow {
        emit(locationService.getCurrentLocation())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val state: StateFlow<StoreState> = flow {
        delay(1000L)
        emit(
            StoreState.Loaded(
                stores = List(10) {
                    StoreModel(
                        id = it.toLong(),
                        name = "서울고물상",
                        imageUrl = "",
                        distance = Distance.meters(20),
                        prices = persistentListOf(
                            MatterModel("고철", 540),
                            MatterModel("고철", 540),
                        )
                    )
                }.toImmutableList()
            )
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), StoreState.Loading)

    fun event(event: HomeScreen.UiEvent) {
        viewModelScope.launch {
            uiEvent.emit(event)
        }
    }
}
