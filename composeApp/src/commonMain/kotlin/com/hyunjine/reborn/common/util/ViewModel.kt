package com.hyunjine.reborn.common.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

abstract class BaseViewModel<T> : ViewModel() {
    protected val uiEvent: MutableSharedFlow<T> = MutableSharedFlow()

    fun <T> Flow<T>.stateIn(
        initialValue: T
    ): StateFlow<T> = this.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = initialValue
    )

    fun event(event: T) {
        viewModelScope.launch {
            uiEvent.emit(event)
        }
    }
}