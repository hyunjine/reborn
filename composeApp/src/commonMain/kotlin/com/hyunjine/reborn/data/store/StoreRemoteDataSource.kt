package com.hyunjine.reborn.data.store

import com.hyunjine.reborn.Location
import com.hyunjine.reborn.entity.StoreEntity
import com.hyunjine.reborn.ui.store_detail.StoreDetailModel

interface StoreRemoteDataSource {
    suspend fun getStores(location: Location): List<StoreEntity>

    suspend fun getStoreDetail(id: Long): StoreDetailModel
}