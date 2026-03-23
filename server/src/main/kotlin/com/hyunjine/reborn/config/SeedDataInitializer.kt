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

/**
 * 개발용 초기 시드 데이터 삽입기.
 *
 * 애플리케이션 시작 시 DB에 업체 데이터가 없으면 샘플 데이터를 삽입합니다.
 * [ExposedConfig]에서 테이블 생성이 완료된 후 실행되도록 [Order]를 2로 설정합니다.
 */
@Component
@Order(2)
class SeedDataInitializer : ApplicationRunner {

    @Transactional
    override fun run(args: ApplicationArguments?) {
        // 이미 데이터가 존재하면 중복 삽입 방지
        if (Stores.selectAll().count() > 0) return

        // 업체 기본 정보 삽입
        val storeId = Stores.insert {
            it[name] = "서울고물상"
            it[address] = "서울특별시 강남구 역삼동 123-45"
            it[description] = "정확한 계근 약속, 대량 매입 시 추가 단가 협의 가능합니다. 30년 전통의 신뢰할 수 있는 고물상입니다."
            it[phoneNumber] = "010-1234-5678"
            it[lastUpdated] = LocalDateTime(2026, 3, 12, 14, 30)
        }[Stores.id]

        // 업체 이미지 4장 삽입
        repeat(4) { index ->
            StoreImages.insert {
                it[this.storeId] = storeId
                it[imageUrl] = "https://picsum.photos/seed/${storeId.value * 10 + index}/200/200"
            }
        }

        // 요일별 영업시간 삽입 (목요일 휴무)
        DayOfWeek.entries.forEach { day ->
            StoreBusinessHours.insert {
                it[this.storeId] = storeId
                it[dayOfWeek] = day.name
                it[isOpen] = day != DayOfWeek.THURSDAY
                it[openTime] = if (day != DayOfWeek.THURSDAY) LocalTime(5, 0) else null
                it[closeTime] = if (day != DayOfWeek.THURSDAY) LocalTime(21, 30) else null
            }
        }

        // 매입 시세 삽입
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
