package com.hyunjine.reborn.config

import com.hyunjine.reborn.store.table.StoreBusinessHours
import com.hyunjine.reborn.store.table.StoreImages
import com.hyunjine.reborn.store.table.StorePrices
import com.hyunjine.reborn.store.table.Stores
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Order(2)
class DatabaseInitializer : ApplicationRunner {

    @Transactional
    override fun run(args: ApplicationArguments?) {
        if (Stores.selectAll().count() > 0) return

        val storeId = Stores.insert {
            it[name] = "서울고물상"
            it[address] = "서울특별시 강남구 역삼동 123-45"
            it[description] = "정확한 계근 약속, 대량 매입 시 추가 단가 협의 가능합니다. 30년 전통의 신뢰할 수 있는 고물상입니다."
            it[phoneNumber] = "010-1234-5678"
            it[lastUpdated] = LocalDateTime(2026, 3, 12, 14, 30)
        }[Stores.id]

        repeat(4) { index ->
            StoreImages.insert {
                it[this.storeId] = storeId
                it[imageUrl] = "https://picsum.photos/seed/${storeId.value * 10 + index}/200/200"
            }
        }

        DayOfWeek.entries.forEach { day ->
            StoreBusinessHours.insert {
                it[this.storeId] = storeId
                it[dayOfWeek] = day.name
                it[isOpen] = day != DayOfWeek.THURSDAY
                it[openTime] = if (day != DayOfWeek.THURSDAY) LocalTime(5, 0) else null
                it[closeTime] = if (day != DayOfWeek.THURSDAY) LocalTime(21, 30) else null
            }
        }

        listOf(
            "고철" to "450원/kg",
            "알루미늄" to "1,800원/kg",
            "구리" to "8,500원/kg",
            "스텐" to "1,150원/kg"
        ).forEach { (itemName, itemPrice) ->
            StorePrices.insert {
                it[this.storeId] = storeId
                it[name] = itemName
                it[price] = itemPrice
            }
        }
    }
}
