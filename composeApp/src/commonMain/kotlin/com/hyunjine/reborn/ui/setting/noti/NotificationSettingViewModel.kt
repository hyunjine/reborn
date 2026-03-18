package com.hyunjine.reborn.ui.setting.noti

import androidx.lifecycle.viewModelScope
import com.hyunjine.reborn.common.util.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class NotificationSettingViewModel(

): BaseViewModel<NotificationSettingScreen.UiEvent>() {
    val isNotificationEnabled: StateFlow<Boolean> = uiEvent
        .filterIsInstance<NotificationSettingScreen.UiEvent.NotificationToggled>()
        .map {
            it.enabled
        }.stateIn(true)

}