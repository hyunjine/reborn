package com.hyunjine.reborn.data.store

import com.hyunjine.reborn.Location
import com.hyunjine.reborn.common.util.ImmutableList
import com.hyunjine.reborn.common.util.now
import com.hyunjine.reborn.data.store.entity.MatterEntity
import com.hyunjine.reborn.data.store.entity.StoreEntity
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
            name = "?úÏö∏Í≥†Î¨º??,
            imageUrls = ImmutableList(4) { "https://picsum.photos/seed/200/200" },
            address = "?úÏö∏?πÎ≥Ñ??Í∞ïÎÇ®Íµ???Çº??123-45",
            description = "?ïÌôï??Í≥ÑÍ∑º ?ΩÏÜç, ?Ä??Îß§ÏûÖ ??Ï∂îÍ? ?®Í? ?ëÏùò Í∞Ä?•Ìï©?àÎã§. 30???ÑÌÜµ???†Î¢∞?????àÎäî Í≥†Î¨º?ÅÏûÖ?àÎã§.",
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
                StorePriceModel("Í≥†Ï≤†", "450??kg"),
                StorePriceModel("?åÎ£®ÎØ∏ÎäÑ", "1,800??kg"),
                StorePriceModel("Íµ¨Î¶¨", "8,500??kg"),
                StorePriceModel("?§ÌÖê", "1,150??kg"),
                StorePriceModel("?§ÌÖê", "1,150??kg"),
                StorePriceModel("?§ÌÖê", "1,150??kg"),
                StorePriceModel("?§ÌÖê", "1,150??kg"),
                StorePriceModel("?§ÌÖê", "1,150??kg"),
                StorePriceModel("?§ÌÖê", "1,150??kg"),
            ),
            lastUpdated = LocalDateTime(2026, 3, 12, 14, 30),
            phoneNumber = "010-1234-5678",
            id = id
        )
    }
}