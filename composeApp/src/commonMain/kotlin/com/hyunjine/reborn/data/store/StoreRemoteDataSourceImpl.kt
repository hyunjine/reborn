package com.hyunjine.reborn.data.store

import com.hyunjine.reborn.data.store.model.store_detail.StoreDetailModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.koin.core.annotation.Single

@Single
class StoreRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : StoreRemoteDataSource {
    override suspend fun getStoreDetail(id: Long): StoreDetailModel {
        return httpClient.get("http://10.0.2.2:8080/api/stores/$id").body()
    }
}
