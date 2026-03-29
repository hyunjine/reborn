package com.hyunjine.reborn.data.store

import com.hyunjine.reborn.data.ApiResponse
import com.hyunjine.reborn.data.Location
import com.hyunjine.reborn.data.store.model.RegistStoreModel
import com.hyunjine.reborn.data.store.model.StoreDetailModel
import com.hyunjine.reborn.data.store.model.StoreModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.collections.immutable.ImmutableList
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
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

    override suspend fun registerStore(model: RegistStoreModel): ApiResponse<Long> {
        val jsonData = buildJsonObject {
            put("name", model.name)
            put("phone", model.phone)
            put("address", model.address)
            put("description", model.description)
            put("daySchedules", buildJsonArray {
                model.daySchedules.forEach { schedule ->
                    add(buildJsonObject {
                        put("dayOfWeek", schedule.dayOfWeek.name)
                        put("isEnabled", schedule.isEnabled)
                        put("startTime", schedule.startTime.toString())
                        put("endTime", schedule.endTime.toString())
                    })
                }
            })
            put("priceItems", buildJsonArray {
                model.priceItems.forEach { item ->
                    add(buildJsonObject {
                        put("name", item.name.value)
                        put("price", item.price)
                        put("unit", "kg")
                    })
                }
            })
        }.toString()

        return httpClient.submitFormWithBinaryData(
            url = "api/stores",
            formData = formData {
                append("data", jsonData, Headers.build {
                    append(HttpHeaders.ContentType, "application/json")
                    append(HttpHeaders.ContentDisposition, "form-data; name=\"data\"")
                })
                model.photos.forEachIndexed { index, bytes ->
                    append("photos", bytes, Headers.build {
                        append(HttpHeaders.ContentType, "image/jpeg")
                        append(
                            HttpHeaders.ContentDisposition,
                            "form-data; name=\"photos\"; filename=\"photo_$index.jpg\""
                        )
                    })
                }
            }
        ).body()
    }
}
