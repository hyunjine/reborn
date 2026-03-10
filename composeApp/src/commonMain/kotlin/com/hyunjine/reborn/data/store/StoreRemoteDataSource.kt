package com.hyunjine.reborn.data.store

import com.hyunjine.reborn.Location
import com.hyunjine.reborn.data.store.entity.StoreEntity

interface StoreRemoteDataSource {
    suspend fun getStores(location: Location): List<StoreEntity>
}