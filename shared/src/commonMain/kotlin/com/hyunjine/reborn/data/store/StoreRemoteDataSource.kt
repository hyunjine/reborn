package com.hyunjine.reborn.data.store

import com.hyunjine.reborn.data.ApiResponse
import com.hyunjine.reborn.data.store.model.store_detail.StoreDetailModel

interface StoreRemoteDataSource {
    suspend fun getStoreDetail(id: Long): ApiResponse<StoreDetailModel>
}