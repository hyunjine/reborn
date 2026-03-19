package com.hyunjine.reborn.data.store

import com.hyunjine.reborn.Location
import com.hyunjine.reborn.common.util.ImmutableList
import com.hyunjine.reborn.entity.MatterEntity
import com.hyunjine.reborn.entity.StoreEntity
import com.hyunjine.reborn.ui.store_detail.Operation
import com.hyunjine.reborn.ui.store_detail.OperationTimeModel
import com.hyunjine.reborn.ui.store_detail.StoreDetailModel
import com.hyunjine.reborn.ui.store_detail.StorePriceModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import org.koin.core.annotation.Single

@Single
class StoreRemoteDataSourceImpl : StoreRemoteDataSource {
    override suspend fun getStores(location: Location): List<StoreEntity> {
        return List(20) {
            StoreEntity(
                id = it.toLong(),
                name = "name $it",
                imageUrl = "https://picsum.photos/seed/200/200",
                distance = 2324,
                prices = List(3) {
                    MatterEntity(name = "name $it", price = 10000)
                }
            )
        }
    }

    override suspend fun getStoreDetail(id: Long): StoreDetailModel {
        return StoreDetailModel(
            name = "서울고물상",
            imageUrls = ImmutableList(4) { "https://picsum.photos/seed/200/200" },
            address = "서울특별시 강남구 역삼동 123-45",
            description = "정확한 계근 약속, 대량 매입 시 추가 단가 협의 가능합니다. 30년 전통의 신뢰할 수 있는 고물상입니다.",
            businessHours = DayOfWeek.entries.map {
                OperationTimeModel(
                    dayOfWeek = it,
                    operation = if (it == DayOfWeek.THURSDAY) {
                        Operation.Closed
                    } else {
                        Operation.Open(
                            start = LocalTime(hour = 5, minute = 0), end = LocalTime(hour = 21, minute = 30)
                        )
                    }
                )
            }.toImmutableList(),
            prices = persistentListOf(
                StorePriceModel("고철", "450원/kg"),
                StorePriceModel("알루미늄", "1,800원/kg"),
                StorePriceModel("구리", "8,500원/kg"),
                StorePriceModel("스텐", "1,150원/kg"),
                StorePriceModel("스텐", "1,150원/kg"),
                StorePriceModel("스텐", "1,150원/kg"),
                StorePriceModel("스텐", "1,150원/kg"),
                StorePriceModel("스텐", "1,150원/kg"),
                StorePriceModel("스텐", "1,150원/kg"),
            ),
            lastUpdated = LocalDateTime(2026, 3, 12, 14, 30),
            phoneNumber = "010-1234-5678",
            id = id
        )
    }
}