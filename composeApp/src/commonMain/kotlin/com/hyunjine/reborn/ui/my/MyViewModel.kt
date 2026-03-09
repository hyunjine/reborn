package com.hyunjine.reborn.ui.my

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MyModel())
    val uiState: StateFlow<MyModel> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<MyScreen.UiEvent>()

    fun event(event: MyScreen.UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
            when (event) {
                is MyScreen.UiEvent.ToggleStoreOpen -> {
                    _uiState.update { state ->
                        state.copy(
                            storeInfo = state.storeInfo?.copy(isOpen = event.isOpen)
                        )
                    }
                }
                else -> {}
            }
        }
    }
}
