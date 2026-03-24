package com.hyunjine.reborn.data.store

import com.hyunjine.reborn.data.ApiResponse
import com.hyunjine.reborn.data.Location
import com.hyunjine.reborn.data.store.model.StoreDetailModel
import com.hyunjine.reborn.data.store.model.StoreModel
import kotlinx.collections.immutable.ImmutableList

interface StoreRemoteDataSource {
    suspend fun getStoreDetail(id: Long): ApiResponse<StoreDetailModel>

    suspend fun getStores(location: Location): ApiResponse<ImmutableList<StoreModel>>
}