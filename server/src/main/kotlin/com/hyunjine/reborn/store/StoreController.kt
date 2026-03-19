package com.hyunjine.reborn.store

import com.hyunjine.reborn.data.store.model.store_detail.Operation
import com.hyunjine.reborn.data.store.model.store_detail.OperationTimeModel
import com.hyunjine.reborn.data.store.model.store_detail.StoreDetailModel
import com.hyunjine.reborn.data.store.model.store_detail.StorePriceModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/stores")
class StoreController {

    @GetMapping("/{id}")
    fun getStoreDetail(@PathVariable id: Long): StoreDetailModel {
        return StoreDetailModel(
            id = id,
            name = "서울고물상",
            imageUrls = List(4) { "https://picsum.photos/seed/200/200" }.toImmutableList(),
            address = "서울특별시 강남구 역삼동 123-45",
            description = "정확한 계근 약속, 대량 매입 시 추가 단가 협의 가능합니다. 30년 전통의 신뢰할 수 있는 고물상입니다.",
            businessHours = DayOfWeek.entries.map {
                OperationTimeModel(
                    dayOfWeek = it,
                    operation = if (it == DayOfWeek.THURSDAY) {
                        Operation.Closed
                    } else {
                        Operation.Open(
                            start = LocalTime(hour = 5, minute = 0),
                            end = LocalTime(hour = 21, minute = 30)
                        )
                    }
                )
            }.toImmutableList(),
            prices = persistentListOf(
                StorePriceModel("고철", "450원/kg"),
                StorePriceModel("알루미늄", "1,800원/kg"),
                StorePriceModel("구리", "8,500원/kg"),
                StorePriceModel("스텐", "1,150원/kg")
            ),
            lastUpdated = LocalDateTime(2026, 3, 12, 14, 30),
            phoneNumber = "010-1234-5678"
        )
    }
}
