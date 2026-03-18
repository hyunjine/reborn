package com.hyunjine.reborn.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

/**
 * ?Œë¦¼ ?¤ì • ?”ë©´??ë¹„ì¦ˆ?ˆìŠ¤ ë¡œì§???´ë‹¹?˜ëŠ” ViewModel.
 * ?Œë¦¼ ?œì„±???íƒœë¥?ê´€ë¦¬í•©?ˆë‹¤.
 */
@KoinViewModel
class SettingViewModel : ViewModel() {

    private val _uiEvent = MutableSharedFlow<NotificationSettingScreen.UiEvent>()

    private val _isNotificationEnabled = MutableStateFlow(true)
    val isNotificationEnabled: StateFlow<Boolean> = _isNotificationEnabled.asStateFlow()

    init {
        _uiEvent
            .filterIsInstance<NotificationSettingScreen.UiEvent.NotificationToggled>()
            .onEach { event -> _isNotificationEnabled.update { event.enabled } }
            .launchIn(viewModelScope)
    }

    /**
     * UI ?´ë²¤?¸ë? ì²˜ë¦¬?©ë‹ˆ??
     * @param event ì²˜ë¦¬??UI ?´ë²¤??
     */
    fun event(event: NotificationSettingScreen.UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }
}
