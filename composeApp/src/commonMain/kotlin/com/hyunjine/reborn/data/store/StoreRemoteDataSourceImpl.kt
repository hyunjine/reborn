package com.hyunjine.reborn.data.store

import com.hyunjine.reborn.data.ApiResponse
import com.hyunjine.reborn.data.Location
import com.hyunjine.reborn.data.store.model.StoreDetailModel
import com.hyunjine.reborn.data.store.model.StoreModel
import com.hyunjine.reborn.util.log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.collections.immutable.ImmutableList
import org.koin.core.annotation.Single

@Single
class StoreRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : StoreRemoteDataSource {
    override suspend fun getStoreDetail(id: Long): ApiResponse<StoreDetailModel> {
        return httpClient.get("api/stores/$id").body()
    }

    override suspend fun getStores(location: Location): ApiResponse<ImmutableList<StoreModel>> {
        return httpClient.get("api/stores") {
            parameter("latitude", location.latitude)
            parameter("longitude", location.longitude)
        }.body()
    }

}
