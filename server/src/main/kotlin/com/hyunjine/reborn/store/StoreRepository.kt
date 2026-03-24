package com.hyunjine.reborn.store

import com.hyunjine.reborn.data.Location
import com.hyunjine.reborn.data.store.model.Distance
import com.hyunjine.reborn.data.store.model.MatterModel
import com.hyunjine.reborn.data.store.model.Operation
import com.hyunjine.reborn.data.store.model.OperationTimeModel
import com.hyunjine.reborn.data.store.model.StoreDetailModel
import com.hyunjine.reborn.data.store.model.StoreModel
import com.hyunjine.reborn.data.store.model.StorePriceModel
import com.hyunjine.reborn.store.table.StoreBusinessHours
import com.hyunjine.reborn.store.table.StoreImages
import com.hyunjine.reborn.store.table.StorePrices
import com.hyunjine.reborn.store.table.Stores
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.DayOfWeek
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import org.springframework.stereotype.Repository
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * 업체 정보를 조회하는 Repository.
 *
 * Exposed DSL을 사용하여 업체 정보를 DB에서 조회하고,
 * shared 모듈의 모델로 변환하여 반환합니다.
 */
@Repository
class StoreRepository {

    /**
     * 모든 업체 목록을 조회합니다.
     *
     * 각 업체의 기본 정보, 대표 이미지, 매입 시세를 조회하고
     * 클라이언트 위치 기반으로 거리를 계산하여 [StoreModel] 목록으로 반환합니다.
     *
     * @param location 클라이언트 위치
     * @return 업체 목록
     */
    suspend fun findAllStores(location: Location): List<StoreModel> = suspendTransaction(readOnly = true) {
        Stores.selectAll().map { store ->
            val storeId = store[Stores.id].value

            val imageUrl = StoreImages.selectAll()
                .where { StoreImages.storeId eq storeId }
                .firstOrNull()
                ?.get(StoreImages.imageUrl) ?: ""

            val prices = StorePrices.selectAll()
                .where { StorePrices.storeId eq storeId }
                .map { row ->
                    MatterModel(
                        name = row[StorePrices.name],
                        price = row[StorePrices.price]
                    )
                }.toImmutableList()

            val distance = calculateDistance(
                lat1 = location.latitude,
                lng1 = location.longitude,
                lat2 = store[Stores.latitude],
                lng2 = store[Stores.longitude]
            )

            StoreModel(
                id = storeId,
                name = store[Stores.name],
                imageUrl = imageUrl,
                distance = Distance.meters(distance),
                prices = prices
            )
        }
    }

    /**
     * 업체 ID로 상세 정보를 조회합니다.
     *
     * 업체 기본 정보, 이미지 목록, 요일별 영업시간, 매입 시세를
     * 각각의 테이블에서 조회한 뒤 [StoreDetailModel]로 조합하여 반환합니다.
     *
     * @param id 조회할 업체 ID
     * @return 업체 상세 모델, 존재하지 않으면 null
     */
    suspend fun findStoreDetailById(id: Long): StoreDetailModel? = suspendTransaction(readOnly = true) {
        val store = Stores.selectAll()
            .where { Stores.id eq id }
            .singleOrNull() ?: return@suspendTransaction null

        val imageUrls = StoreImages.selectAll()
            .where { StoreImages.storeId eq id }
            .map { it[StoreImages.imageUrl] }
            .toImmutableList()

        val businessHours = StoreBusinessHours.selectAll()
            .where { StoreBusinessHours.storeId eq id }
            .map { row ->
                OperationTimeModel(
                    dayOfWeek = DayOfWeek.valueOf(row[StoreBusinessHours.dayOfWeek]),
                    operation = if (row[StoreBusinessHours.isOpen]) {
                        Operation.Open(
                            start = row[StoreBusinessHours.openTime]!!,
                            end = row[StoreBusinessHours.closeTime]!!
                        )
                    } else {
                        Operation.Closed
                    }
                )
            }.toImmutableList()

        val prices = StorePrices.selectAll()
            .where { StorePrices.storeId eq id }
            .map { row ->
                StorePriceModel(
                    name = row[StorePrices.name],
                    price = row[StorePrices.price],
                    unit = row[StorePrices.unit]
                )
            }.toImmutableList()

        StoreDetailModel(
            id = store[Stores.id].value,
            name = store[Stores.name],
            imageUrls = imageUrls,
            address = store[Stores.address],
            description = store[Stores.description],
            businessHours = businessHours,
            prices = prices,
            lastUpdated = store[Stores.lastUpdated],
            phoneNumber = store[Stores.phoneNumber]
        )
    }

    companion object {
        private const val EARTH_RADIUS_METERS = 6_371_000.0

        /**
         * Haversine 공식으로 두 좌표 간 거리를 미터 단위로 계산합니다.
         */
        private fun calculateDistance(
            lat1: Double, lng1: Double,
            lat2: Double, lng2: Double
        ): Int {
            val dLat = Math.toRadians(lat2 - lat1)
            val dLng = Math.toRadians(lng2 - lng1)
            val a = sin(dLat / 2) * sin(dLat / 2) +
                    cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                    sin(dLng / 2) * sin(dLng / 2)
            val c = 2 * atan2(sqrt(a), sqrt(1 - a))
            return (EARTH_RADIUS_METERS * c).toInt()
        }
    }
}
