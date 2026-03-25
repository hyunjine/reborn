package com.hyunjine.reborn.data.store

import com.hyunjine.reborn.data.Location
import com.hyunjine.reborn.data.ApiResponse
import com.hyunjine.reborn.data.store.model.RegistStoreModel
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

    suspend fun registerStore(
        model: RegistStoreModel,
        latitude: Double,
        longitude: Double
    ): ApiResponse<Long> {
        return storeRemoteDataSource.registerStore(model, latitude, longitude)
    }
}