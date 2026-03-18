package com.hyunjine.reborn.ui.my

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.annotation.KoinViewModel

/**
 * ???•ë³´ ?”ë©´??ë¹„ì¦ˆ?ˆìŠ¤ ë¡œì§???´ë‹¹?˜ëŠ” ViewModel.
 * ?¬ìš©???•ë³´?€ ?…ì²´ ?±ë¡ ?íƒœë¥?ê´€ë¦¬í•©?ˆë‹¤.
 */
@KoinViewModel
class MyViewModel : ViewModel() {

    private val _state = MutableStateFlow(
        MyModel(
            userName = "ê¹€ì² ìˆ˜",
            email = "kimcs@example.com",
            hasStore = false,
            storeInfo = null
        )
    )
    val state: StateFlow<MyModel> = _state.asStateFlow()
}
