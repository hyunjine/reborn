package com.hyunjine.reborn.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyunjine.reborn.LocationService
import com.hyunjine.reborn.data.ApiResponse
import com.hyunjine.reborn.data.Location
import com.hyunjine.reborn.data.store.StoreRemoteDataSource
import com.hyunjine.reborn.data.store.model.StoreModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(
    private val locationService: LocationService,
    private val storeRemoteDataSource: StoreRemoteDataSource
) : ViewModel() {
    private val uiEvent = MutableSharedFlow<HomeScreen.UiEvent>()

    val location: StateFlow<Location?> = flow {
        emit(locationService.getCurrentLocation())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val state: StateFlow<ApiResponse<ImmutableList<StoreModel>>> = location
        .filterNotNull()
        .map { location ->
            storeRemoteDataSource.getStores(location)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ApiResponse.Loading)


    fun event(event: HomeScreen.UiEvent) {
        viewModelScope.launch {
            uiEvent.emit(event)
        }
    }
}
