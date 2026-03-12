package com.hyunjine.reborn.data.store

import com.hyunjine.reborn.Location
import com.hyunjine.reborn.data.store.entity.MatterEntity
import com.hyunjine.reborn.data.store.entity.StoreEntity
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
}