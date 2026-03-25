package com.hyunjine.reborn.ui.regist_store

import androidx.lifecycle.viewModelScope
import com.hyunjine.reborn.common.util.BaseViewModel
import com.hyunjine.reborn.data.store.StoreRepository
import com.hyunjine.reborn.data.store.model.PriceItemModel
import com.hyunjine.reborn.data.store.model.RegistStoreModel
import com.hyunjine.reborn.ui.regist_store.RegistStoreScreen.UiEvent
import com.hyunjine.reborn.util.mapStable
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.runningFold
import kotlinx.datetime.LocalTime
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class RegistStoreViewModel(
    private val repository: StoreRepository
) : BaseViewModel<UiEvent>() {
    sealed interface Effect {
        data class ShowSnackbar(val message: String) : Effect
    }

    val effects: ReceiveChannel<Effect>
        field: Channel<Effect> = Channel()

    val model: StateFlow<RegistStoreModel> = uiEvent
        .runningFold(RegistStoreModel()) { old, event ->
            when (event) {
                is UiEvent.PhotosAdded -> old.copy(photos = event.photos.toImmutableList())
                is UiEvent.PhotoRemoved -> old.copy(
                    photos = old.photos.filterIndexed { i, _ ->
                        i != event.index
                    }.toImmutableList()
                )
                is UiEvent.StoreNameChanged -> old.copy(name = event.name)
                is UiEvent.PhoneChanged -> old.copy(phone = event.phone)
                is UiEvent.AddressChanged -> old.copy(address = event.address)
                is UiEvent.DescriptionChanged -> old.copy(description = event.description)
                is UiEvent.BatchStartTimeChanged -> {
                    val is24Hour = event.time == LocalTime(0, 0) && old.batchEndTime == LocalTime(0, 0)
                    if (!is24Hour && event.time >= old.batchEndTime) {
                        effects.send(Effect.ShowSnackbar("시작 시간은 종료 시간보다 빨라야 합니다."))
                        old
                    } else {
                        old.copy(batchStartTime = event.time)
                    }
                }
                is UiEvent.BatchEndTimeChanged -> {
                    val is24Hour = old.batchStartTime == LocalTime(0, 0) && event.time == LocalTime(0, 0)
                    if (!is24Hour && event.time <= old.batchStartTime) {
                        effects.send(Effect.ShowSnackbar("종료 시간은 시작 시간보다 늦어야 합니다."))
                        old
                    } else {
                        old.copy(batchEndTime = event.time)
                    }
                }
                is UiEvent.ApplyBatchTime -> old.copy(
                    daySchedules = old.daySchedules.mapStable { schedule ->
                        schedule.copy(
                            startTime = old.batchStartTime,
                            endTime = old.batchEndTime
                        )
                    }
                )
                is UiEvent.DayEnabledChanged -> old.copy(
                    daySchedules = old.daySchedules.mapStable { schedule ->
                        if (schedule.dayOfWeek == event.key) schedule.copy(isEnabled = event.enabled) else schedule
                    }
                )
                is UiEvent.DayStartTimeChanged -> old.copy(
                    daySchedules = old.daySchedules.mapStable { schedule ->
                        if (schedule.dayOfWeek == event.key) schedule.copy(startTime = event.time) else schedule
                    }
                )
                is UiEvent.DayEndTimeChanged -> old.copy(
                    daySchedules = old.daySchedules.mapStable { schedule ->
                        if (schedule.dayOfWeek == event.key) schedule.copy(endTime = event.time) else schedule
                    }
                )
                is UiEvent.AddPriceItem -> old.copy(
                    priceItems = (old.priceItems + PriceItemModel()).toImmutableList()
                )
                is UiEvent.RemovePriceItem -> old.copy(
                    priceItems = old.priceItems.filterIndexed { i, _ ->
                        i != event.index
                    }.toImmutableList()
                )
                is UiEvent.PriceItemNameChanged -> old.copy(
                    priceItems = old.priceItems.mapIndexed { i, p ->
                        if (i == event.index) p.copy(name = event.name) else p
                    }.toImmutableList()
                )
                is UiEvent.PriceItemPriceChanged -> old.copy(
                    priceItems = old.priceItems.mapIndexed { i, p ->
                        if (i == event.index) p.copy(price = event.price) else p
                    }.toImmutableList()
                )
                else -> old
            }
        }.stateIn(RegistStoreModel())

    private val submitEvent = uiEvent
        .filterIsInstance<UiEvent.SubmitClicked>()
        .onEach {
            val message = model.value.isValid()
            if (message == null) {
//                repository.addStore(model.value)
                effects.send(Effect.ShowSnackbar("등록 완료!"))
            } else {
                effects.send(Effect.ShowSnackbar(message))
            }
        }.launchIn(viewModelScope)

    val addressWindowState: StateFlow<Boolean> = uiEvent
        .filterIsInstance<UiEvent.AddressSearchState>()
        .map { event -> event.isShow }
        .stateIn(false)

}
