package com.hyunjine.reborn.data.store

import com.hyunjine.reborn.data.Location
import com.hyunjine.reborn.data.ApiResponse
import com.hyunjine.reborn.data.store.model.StoreDetailModel
import com.hyunjine.reborn.data.store.model.StoreModel
import org.koin.core.annotation.Single

@Single
class StoreRepository(
    private val storeRemoteDataSource: StoreRemoteDataSource
) {
    suspend fun getStores(location: Location): ApiResponse<List<StoreModel>> {
        return storeRemoteDataSource.getStores(location)
    }

    suspend fun getStoreDetail(id: Long): ApiResponse<StoreDetailModel> {
        return storeRemoteDataSource.getStoreDetail(id)
    }
}