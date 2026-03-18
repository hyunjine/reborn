package com.hyunjine.reborn.ui.my

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

/**
 * 내 정보 화면의 비즈니스 로직을 담당하는 ViewModel.
 * 사용자 정보와 업체 등록 상태를 관리합니다.
 */
@KoinViewModel
class MyViewModel : ViewModel() {

    private val _uiEvent = MutableSharedFlow<MyScreen.UiEvent>()

    private val _state = MutableStateFlow(
        MyModel(
            userName = "김철수",
            email = "kimcs@example.com",
            hasStore = false,
            storeInfo = null
        )
    )
    val state: StateFlow<MyModel> = _state.asStateFlow()

    /**
     * UI 이벤트를 처리합니다.
     * @param event 처리할 UI 이벤트.
     */
    fun event(event: MyScreen.UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }
}
