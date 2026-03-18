package com.hyunjine.reborn.ui.my

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.annotation.KoinViewModel

/**
 * 내 정보 화면의 비즈니스 로직을 담당하는 ViewModel.
 * 사용자 정보와 업체 등록 상태를 관리합니다.
 */
@KoinViewModel
class MyViewModel : ViewModel() {

    private val _state = MutableStateFlow(
        MyModel(
            userName = "김철수",
            email = "kimcs@example.com",
            hasStore = false,
            storeInfo = null
        )
    )
    val state: StateFlow<MyModel> = _state.asStateFlow()
}
