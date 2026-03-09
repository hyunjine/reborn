package com.hyunjine.reborn.ui.regist_store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RegistStoreViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegistStoreModel())
    val uiState: StateFlow<RegistStoreModel> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<RegistStoreScreen.UiEvent>()

    fun event(event: RegistStoreScreen.UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
            when (event) {
                is RegistStoreScreen.UiEvent.StoreNameChanged -> {
                    _uiState.update { it.copy(storeName = event.name) }
                }
                is RegistStoreScreen.UiEvent.AddressChanged -> {
                    _uiState.update { it.copy(address = event.address) }
                }
                is RegistStoreScreen.UiEvent.DescriptionChanged -> {
                    _uiState.update { it.copy(description = event.description) }
                }
                is RegistStoreScreen.UiEvent.BatchStartTimeChanged -> {
                    _uiState.update { it.copy(batchStartTime = event.time) }
                }
                is RegistStoreScreen.UiEvent.BatchEndTimeChanged -> {
                    _uiState.update { it.copy(batchEndTime = event.time) }
                }
                is RegistStoreScreen.UiEvent.ApplyBatchTime -> {
                    _uiState.update { state ->
                        state.copy(
                            daySchedules = state.daySchedules.map { schedule ->
                                if (schedule.isEnabled) {
                                    schedule.copy(
                                        startTime = state.batchStartTime,
                                        endTime = state.batchEndTime
                                    )
                                } else schedule
                            }.toImmutableList()
                        )
                    }
                }
                is RegistStoreScreen.UiEvent.DayEnabledChanged -> {
                    _uiState.update { state ->
                        state.copy(
                            daySchedules = state.daySchedules.mapIndexed { i, s ->
                                if (i == event.index) s.copy(isEnabled = event.enabled) else s
                            }.toImmutableList()
                        )
                    }
                }
                is RegistStoreScreen.UiEvent.DayStartTimeChanged -> {
                    _uiState.update { state ->
                        state.copy(
                            daySchedules = state.daySchedules.mapIndexed { i, s ->
                                if (i == event.index) s.copy(startTime = event.time) else s
                            }.toImmutableList()
                        )
                    }
                }
                is RegistStoreScreen.UiEvent.DayEndTimeChanged -> {
                    _uiState.update { state ->
                        state.copy(
                            daySchedules = state.daySchedules.mapIndexed { i, s ->
                                if (i == event.index) s.copy(endTime = event.time) else s
                            }.toImmutableList()
                        )
                    }
                }
                is RegistStoreScreen.UiEvent.HolidayClosedChanged -> {
                    _uiState.update { it.copy(isHolidayClosed = event.closed) }
                }
                is RegistStoreScreen.UiEvent.AddPriceItem -> {
                    _uiState.update { state ->
                        state.copy(
                            priceItems = (state.priceItems + PriceItemModel()).toImmutableList()
                        )
                    }
                }
                is RegistStoreScreen.UiEvent.PriceItemNameChanged -> {
                    _uiState.update { state ->
                        state.copy(
                            priceItems = state.priceItems.mapIndexed { i, p ->
                                if (i == event.index) p.copy(name = event.name) else p
                            }.toImmutableList()
                        )
                    }
                }
                is RegistStoreScreen.UiEvent.PriceItemPriceChanged -> {
                    _uiState.update { state ->
                        state.copy(
                            priceItems = state.priceItems.mapIndexed { i, p ->
                                if (i == event.index) p.copy(price = event.price) else p
                            }.toImmutableList()
                        )
                    }
                }
                else -> {}
            }
        }
    }
}
