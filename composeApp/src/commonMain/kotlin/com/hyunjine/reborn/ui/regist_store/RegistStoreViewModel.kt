package com.hyunjine.reborn.ui.regist_store

import androidx.lifecycle.viewModelScope
import com.hyunjine.reborn.common.util.BaseViewModel
import com.hyunjine.reborn.ui.regist_store.RegistStoreScreen.UiEvent
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class RegistStoreViewModel : BaseViewModel() {

    sealed interface Effect {
        data class ShowSnackbar(val message: String) : Effect
    }

    private val _uiState = MutableStateFlow(RegistStoreModel())
    val uiState: StateFlow<RegistStoreModel> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<Effect>()
    val effect: SharedFlow<Effect> = _effect.asSharedFlow()

    private val uiEvent = MutableSharedFlow<UiEvent>()

    init {
        uiEvent.filterIsInstance<UiEvent.PhotosAdded>()
            .onEach { event ->
                _uiState.update { state ->
                    val newPhotos = (state.photos + event.photos)
                        .take(state.maxPhotoCount)
                        .toImmutableList()
                    state.copy(photos = newPhotos)
                }
            }.launchIn(viewModelScope)

        uiEvent.filterIsInstance<UiEvent.PhotoRemoved>()
            .onEach { event ->
                _uiState.update { state ->
                    state.copy(
                        photos = state.photos.filterIndexed { i, _ ->
                            i != event.index
                        }.toImmutableList()
                    )
                }
            }.launchIn(viewModelScope)

        uiEvent.filterIsInstance<UiEvent.StoreNameChanged>()
            .onEach { event -> _uiState.update { it.copy(name = event.name) } }
            .launchIn(viewModelScope)

        uiEvent.filterIsInstance<UiEvent.PhoneChanged>()
            .onEach { event -> _uiState.update { it.copy(phone = event.phone) } }
            .launchIn(viewModelScope)

        uiEvent.filterIsInstance<UiEvent.AddressChanged>()
            .onEach { event -> _uiState.update { it.copy(address = event.address) } }
            .launchIn(viewModelScope)

        uiEvent.filterIsInstance<UiEvent.DescriptionChanged>()
            .onEach { event -> _uiState.update { it.copy(description = event.description) } }
            .launchIn(viewModelScope)

        uiEvent.filterIsInstance<UiEvent.BatchStartTimeChanged>()
            .onEach { event ->
                _uiState.update { state ->
                    if (isValidTimeRange(event.time, state.batchEndTime)) {
                        state.copy(batchStartTime = event.time)
                    } else {
                        viewModelScope.launch { _effect.emit(Effect.ShowSnackbar("시작 시간은 종료 시간보다 빨라야 합니다.")) }
                        state
                    }
                }
            }
            .launchIn(viewModelScope)

        uiEvent.filterIsInstance<UiEvent.BatchEndTimeChanged>()
            .onEach { event ->
                _uiState.update { state ->
                    if (isValidTimeRange(state.batchStartTime, event.time)) {
                        state.copy(batchEndTime = event.time)
                    } else {
                        viewModelScope.launch { _effect.emit(Effect.ShowSnackbar("종료 시간은 시작 시간보다 늦어야 합니다.")) }
                        state
                    }
                }
            }
            .launchIn(viewModelScope)

        uiEvent.filterIsInstance<UiEvent.ApplyBatchTime>()
            .onEach {
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
            }.launchIn(viewModelScope)

        uiEvent.filterIsInstance<UiEvent.DayEnabledChanged>()
            .onEach { event ->
                _uiState.update { state ->
                    state.copy(
                        daySchedules = state.daySchedules.mapIndexed { i, s ->
                            if (i == event.index) s.copy(isEnabled = event.enabled) else s
                        }.toImmutableList()
                    )
                }
            }.launchIn(viewModelScope)

        uiEvent.filterIsInstance<UiEvent.DayStartTimeChanged>()
            .onEach { event ->
                _uiState.update { state ->
                    val schedule = state.daySchedules[event.index]
                    if (isValidTimeRange(event.time, schedule.endTime)) {
                        state.copy(
                            daySchedules = state.daySchedules.mapIndexed { i, s ->
                                if (i == event.index) s.copy(startTime = event.time) else s
                            }.toImmutableList()
                        )
                    } else {
                        viewModelScope.launch { _effect.emit(Effect.ShowSnackbar("시작 시간은 종료 시간보다 빨라야 합니다.")) }
                        state
                    }
                }
            }
            .launchIn(viewModelScope)

        uiEvent.filterIsInstance<UiEvent.DayEndTimeChanged>()
            .onEach { event ->
                _uiState.update { state ->
                    val schedule = state.daySchedules[event.index]
                    if (isValidTimeRange(schedule.startTime, event.time)) {
                        state.copy(
                            daySchedules = state.daySchedules.mapIndexed { i, s ->
                                if (i == event.index) s.copy(endTime = event.time) else s
                            }.toImmutableList()
                        )
                    } else {
                        viewModelScope.launch { _effect.emit(Effect.ShowSnackbar("종료 시간은 시작 시간보다 늦어야 합니다.")) }
                        state
                    }
                }
            }
            .launchIn(viewModelScope)

        uiEvent.filterIsInstance<UiEvent.AddPriceItem>()
            .onEach {
                _uiState.update { state ->
                    state.copy(
                        priceItems = (state.priceItems + PriceItemModel()).toImmutableList()
                    )
                }
            }.launchIn(viewModelScope)

        uiEvent.filterIsInstance<UiEvent.RemovePriceItem>()
            .onEach { event ->
                _uiState.update { state ->
                    state.copy(
                        priceItems = state.priceItems.filterIndexed { i, _ ->
                            i != event.index
                        }.toImmutableList()
                    )
                }
            }.launchIn(viewModelScope)

        uiEvent.filterIsInstance<UiEvent.PriceItemNameChanged>()
            .onEach { event ->
                _uiState.update { state ->
                    state.copy(
                        priceItems = state.priceItems.mapIndexed { i, p ->
                            if (i == event.index) p.copy(name = event.name) else p
                        }.toImmutableList()
                    )
                }
            }.launchIn(viewModelScope)

        uiEvent.filterIsInstance<UiEvent.PriceItemCustomNameChanged>()
            .onEach { event ->
                _uiState.update { state ->
                    state.copy(
                        priceItems = state.priceItems.mapIndexed { i, p ->
                            if (i == event.index) p.copy(customName = event.customName) else p
                        }.toImmutableList()
                    )
                }
            }.launchIn(viewModelScope)

        uiEvent.filterIsInstance<UiEvent.PriceItemPriceChanged>()
            .onEach { event ->
                _uiState.update { state ->
                    state.copy(
                        priceItems = state.priceItems.mapIndexed { i, p ->
                            if (i == event.index) p.copy(price = event.price) else p
                        }.toImmutableList()
                    )
                }
            }.launchIn(viewModelScope)

        uiEvent.filterIsInstance<UiEvent.AddressSearchState>()
            .onEach { event ->
                _uiState.update { it.copy(isShowingAddressSearch = event.isShow) }
            }.launchIn(viewModelScope)
    }

    private fun isValidTimeRange(start: String, end: String): Boolean {
        if (start.isEmpty() || end.isEmpty()) return true
        val startMinutes = timeToMinutes(start)
        val endMinutes = timeToMinutes(end)
        return startMinutes < endMinutes
    }

    private fun timeToMinutes(time: String): Int {
        val parts = time.split(":")
        if (parts.size != 2) return 0
        return parts[0].toInt() * 60 + parts[1].toInt()
    }

    fun event(event: UiEvent) {
        viewModelScope.launch {
            uiEvent.emit(event)
        }
    }
}
