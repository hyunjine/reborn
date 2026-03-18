package com.hyunjine.reborn.ui.regist_store

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import com.hyunjine.reborn.common.util.BaseViewModel
import com.hyunjine.reborn.data.store.StoreRepository
import com.hyunjine.reborn.ui.regist_store.RegistStoreScreen.UiEvent
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentHashMap
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalTime
import org.koin.core.annotation.KoinViewModel
import kotlin.collections.map
import kotlin.collections.plus

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
                        effects.send(Effect.ShowSnackbar("?úÏûë ?úÍ∞Ñ?Ä Ï¢ÖÎ£å ?úÍ∞ÑÎ≥¥Îã§ Îπ®Îùº???©Îãà??"))
                        old
                    } else {
                        old.copy(batchStartTime = event.time)
                    }
                }
                is UiEvent.BatchEndTimeChanged -> {
                    val is24Hour = old.batchStartTime == LocalTime(0, 0) && event.time == LocalTime(0, 0)
                    if (!is24Hour && event.time <= old.batchStartTime) {
                        effects.send(Effect.ShowSnackbar("Ï¢ÖÎ£å ?úÍ∞Ñ?Ä ?úÏûë ?úÍ∞ÑÎ≥¥Îã§ ??ñ¥???©Îãà??"))
                        old
                    } else {
                        old.copy(batchEndTime = event.time)
                    }
                }
                is UiEvent.ApplyBatchTime -> old.copy(
                    daySchedules = old.daySchedules.mapValues { schedule ->
                        schedule.value.copy(
                            startTime = old.batchStartTime,
                            endTime = old.batchEndTime
                        )
                    }.toPersistentHashMap()
                )
                is UiEvent.DayEnabledChanged -> old.copy(
                    daySchedules = old.daySchedules.mapValues { schedule ->
                        if (schedule.key == event.key) schedule.value.copy(isEnabled = event.enabled) else schedule.value
                    }.toPersistentHashMap()
                )
                is UiEvent.DayStartTimeChanged -> old.copy(
                    daySchedules = old.daySchedules.mapValues { schedule ->
                        if (schedule.key == event.key) schedule.value.copy(startTime = event.time) else schedule.value
                    }.toPersistentHashMap()
                )
                is UiEvent.DayEndTimeChanged -> old.copy(
                    daySchedules = old.daySchedules.mapValues { schedule ->
                        if (schedule.key == event.key) schedule.value.copy(endTime = event.time) else schedule.value
                    }.toPersistentHashMap()
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
                effects.send(Effect.ShowSnackbar("?±Î°ù ?ÑÎ£å!"))
            } else {
                effects.send(Effect.ShowSnackbar(message))
            }
        }.launchIn(viewModelScope)

    val addressWindowState: StateFlow<Boolean> = uiEvent
        .filterIsInstance<UiEvent.AddressSearchState>()
        .map { event -> event.isShow }
        .stateIn(false)

}
