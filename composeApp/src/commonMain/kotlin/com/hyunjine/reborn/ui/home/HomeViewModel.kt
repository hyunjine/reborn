package com.hyunjine.reborn.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeModel())
    val uiState: StateFlow<HomeModel> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<HomeScreen.UiEvent>()

    init {
        // Mock Data based on Figma
        _uiState.update { 
            it.copy(
                centers = persistentListOf(
                    GarbageCenterModel(
                        id = 1,
                        name = "서울고물상",
                        imageUrl = "",
                        distance = "0.2km",
                        prices = persistentListOf(
                            PriceModel("고철", "450원/kg"),
                            PriceModel("알루미늄", "1,800원/kg")
                        )
                    ),
                    GarbageCenterModel(
                        id = 2,
                        name = "금성스크랩",
                        imageUrl = "",
                        distance = "0.8km",
                        prices = persistentListOf(
                            PriceModel("고철", "480원/kg"),
                            PriceModel("스텐", "1,200원/kg")
                        )
                    ),
                    GarbageCenterModel(
                        id = 3,
                        name = "대한재활용센터",
                        imageUrl = "",
                        distance = "1.2km",
                        prices = persistentListOf(
                            PriceModel("구리", "8,800원/kg"),
                            PriceModel("황동", "5,200원/kg")
                        )
                    ),
                    GarbageCenterModel(
                        id = 4,
                        name = "진성고물상",
                        imageUrl = "",
                        distance = "1.5km",
                        prices = persistentListOf(
                            PriceModel("고철", "470원/kg"),
                            PriceModel("구리", "8,600원/kg")
                        )
                    ),
                    GarbageCenterModel(
                        id = 5,
                        name = "한강재활용",
                        imageUrl = "",
                        distance = "2.3km",
                        prices = persistentListOf(
                            PriceModel("기판", "12,000원/kg"),
                            PriceModel("알루미늄", "1,750원/kg")
                        )
                    ),
                    GarbageCenterModel(
                        id = 6,
                        name = "강남스크랩",
                        imageUrl = "",
                        distance = "3.1km",
                        prices = persistentListOf(
                            PriceModel("구리", "8,700원/kg"),
                            PriceModel("스텐", "1,300원/kg")
                        )
                    )
                )
            )
        }
    }

    fun event(event: HomeScreen.UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
            when (event) {
                is HomeScreen.UiEvent.FilterSelected -> {
                    _uiState.update { it.copy(selectedFilter = event.filter) }
                }
                else -> {}
            }
        }
    }
}
