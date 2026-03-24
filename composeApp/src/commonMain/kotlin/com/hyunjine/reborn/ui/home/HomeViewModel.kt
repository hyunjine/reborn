package com.hyunjine.reborn.ui.home

import androidx.lifecycle.viewModelScope
import com.hyunjine.reborn.LocationService
import com.hyunjine.reborn.common.util.BaseViewModel
import com.hyunjine.reborn.data.ApiResponse
import com.hyunjine.reborn.data.Location
import com.hyunjine.reborn.data.store.StoreRemoteDataSource
import com.hyunjine.reborn.data.store.model.StoreModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(
    private val locationService: LocationService,
    private val storeRemoteDataSource: StoreRemoteDataSource
) : BaseViewModel<HomeScreen.UiEvent>() {

    val location: StateFlow<Location?> = flow {
        emit(locationService.getCurrentLocation())
    }.stateIn(null)

    val state: StateFlow<ApiResponse<ImmutableList<StoreModel>>> = location
        .filterNotNull()
        .map { location ->
            storeRemoteDataSource.getStores(location)
        }.stateIn(ApiResponse.Loading)
}
