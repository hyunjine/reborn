package com.hyunjine.reborn.data.store

import com.hyunjine.reborn.Location
import com.hyunjine.reborn.model.store_detail.StoreDetailModel
import com.hyunjine.reborn.ui.home.Distance
import com.hyunjine.reborn.ui.home.MatterModel
import com.hyunjine.reborn.ui.home.StoreModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.koin.core.annotation.Single

@Single
class StoreRepository(
    private val storeRemoteDataSource: StoreRemoteDataSource
) {
    suspend fun getCurrentLocation(): Location? {
        return Location(36.9638808, 127.9429039)
    }

    suspend fun getStores(location: Location): ImmutableList<StoreModel> {
        return storeRemoteDataSource.getStores(location).map { entity ->
            StoreModel(
                id = entity.id,
                name = entity.name,
                imageUrl = entity.imageUrl,
                distance = Distance.meters(entity.distance),
                prices = entity.prices.map {
                    MatterModel(
                        name = it.name, price = it.price
                    )
                }.toImmutableList()
            )
        }.toImmutableList()
    }

    suspend fun getStoreDetail(id: Long): StoreDetailModel {
        return storeRemoteDataSource.getStoreDetail(id)
    }
}