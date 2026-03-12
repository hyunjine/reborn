package com.hyunjine.reborn.data.store

import com.hyunjine.reborn.Location
import com.hyunjine.reborn.data.store.entity.StoreEntity
import org.koin.core.annotation.Single

@Single
class StoreRemoteDataSourceImpl: StoreRemoteDataSource {
    override suspend fun getStores(location: Location): List<StoreEntity> {
        return List(20) {
            StoreEntity(id = it.toLong(), name = "name", imageUrl = "https://picsum.photos/seed/$it/200", distance = 2324, prices = emptyList())
        }
    }
}