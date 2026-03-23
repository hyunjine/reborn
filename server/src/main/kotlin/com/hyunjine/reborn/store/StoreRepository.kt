package com.hyunjine.reborn.store

import com.hyunjine.reborn.data.store.model.store_detail.Operation
import com.hyunjine.reborn.data.store.model.store_detail.OperationTimeModel
import com.hyunjine.reborn.data.store.model.store_detail.StoreDetailModel
import com.hyunjine.reborn.data.store.model.store_detail.StorePriceModel
import com.hyunjine.reborn.store.table.StoreBusinessHours
import com.hyunjine.reborn.store.table.StoreImages
import com.hyunjine.reborn.store.table.StorePrices
import com.hyunjine.reborn.store.table.Stores
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.DayOfWeek
import org.jetbrains.exposed.sql.selectAll
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

/**
 * 업체 정보를 조회하는 Repository.
 *
 * Exposed DSL을 사용하여 업체 상세 정보를 DB에서 조회하고,
 * shared 모듈의 [StoreDetailModel]로 변환하여 반환합니다.
 */
@Repository
class StoreRepository {

    /**
     * 업체 ID로 상세 정보를 조회합니다.
     *
     * 업체 기본 정보, 이미지 목록, 요일별 영업시간, 매입 시세를
     * 각각의 테이블에서 조회한 뒤 [StoreDetailModel]로 조합하여 반환합니다.
     *
     * @param id 조회할 업체 ID
     * @return 업체 상세 모델, 존재하지 않으면 null
     */
    @Transactional(readOnly = true)
    fun findStoreDetailById(id: Long): StoreDetailModel? {
        val store = Stores.selectAll()
            .where { Stores.id eq id }
            .singleOrNull() ?: return null

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
                    price = row[StorePrices.price]
                )
            }.toImmutableList()

        return StoreDetailModel(
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
}
