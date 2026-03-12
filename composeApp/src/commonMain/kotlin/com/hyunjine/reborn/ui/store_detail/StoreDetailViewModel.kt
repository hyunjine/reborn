package com.hyunjine.reborn.ui.store_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class StoreDetailViewModel(
    private val storeId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(StoreDetailModel())
    val uiState: StateFlow<StoreDetailModel> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<StoreDetailScreen.UiEvent>()

    init {
        loadStoreDetail()
    }

    fun event(event: StoreDetailScreen.UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }

    private fun loadStoreDetail() {
        // Mock Data based on Figma
        _uiState.update {
            StoreDetailModel(
                name = "서울고물상",
                isVerified = true,
                address = "서울특별시 강남구 역삼동 123-45",
                description = "정확한 계근 약속, 대량 매입 시 추가 단가 협의 가능합니다. 30년 전통의 신뢰할 수 있는 고물상입니다.",
                businessHours = persistentListOf(
                    BusinessHourModel("월요일", "08:00 - 18:00"),
                    BusinessHourModel("화요일", "08:00 - 18:00"),
                    BusinessHourModel("수요일", "08:00 - 18:00"),
                    BusinessHourModel("목요일", "08:00 - 18:00"),
                    BusinessHourModel("금요일", "08:00 - 18:00"),
                    BusinessHourModel("토요일", "08:00 - 15:00"),
                    BusinessHourModel("일요일", "08:00 - 15:00"),
                    BusinessHourModel("공휴일", "영업 종료")
                ),
                prices = persistentListOf(
                    StorePriceModel("고철", "450원/kg"),
                    StorePriceModel("알루미늄", "1,800원/kg"),
                    StorePriceModel("구리", "8,500원/kg"),
                    StorePriceModel("스텐", "1,150원/kg"),
                    StorePriceModel("황동", "5,100원/kg"),
                    StorePriceModel("전선", "3,400원/kg"),
                    StorePriceModel("기판", "11,800원/kg"),
                    StorePriceModel("파지", "120원/kg")
                ),
                lastUpdated = "최종 업데이트: 2시간 전",
                phoneNumber = "02-1234-5678"
            )
        }
    }
}
