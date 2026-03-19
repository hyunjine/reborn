package com.hyunjine.reborn.data.store

import com.hyunjine.reborn.Location
import com.hyunjine.reborn.data.store.model.store_detail.StoreDetailModel
import com.hyunjine.reborn.ui.home.Distance
import com.hyunjine.reborn.ui.home.MatterModel
import com.hyunjine.reborn.ui.home.StoreModel
import com.hyunjine.reborn.util.ImmutableList
import kotlinx.collections.immutable.ImmutableList
import org.koin.core.annotation.Single
import kotlin.random.Random

@Single
class StoreRepository(
    private val storeRemoteDataSource: StoreRemoteDataSource
) {
    suspend fun getCurrentLocation(): Location? {
        return Location(36.9638808, 127.9429039)
    }

    suspend fun getStores(location: Location): ImmutableList<StoreModel> {
        return ImmutableList(20) {
            StoreModel(
                id = it.toLong(),
                name = "name $it",
                imageUrl = "https://picsum.photos/seed/200/200",
                distance = Distance.meters(Random.nextInt(100, 100000)),
                prices = ImmutableList(3) {
                    MatterModel(
                        name = "name $it", price = Random.nextInt(100, 100000)
                    )
                }
            )
        }
    }

    suspend fun getStoreDetail(id: Long): StoreDetailModel {
        return storeRemoteDataSource.getStoreDetail(id)
    }
}