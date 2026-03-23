package com.hyunjine.reborn.config

import com.hyunjine.reborn.store.table.StoreBusinessHours
import com.hyunjine.reborn.store.table.StoreImages
import com.hyunjine.reborn.store.table.StorePrices
import com.hyunjine.reborn.store.table.Stores
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/**
 * Exposed 테이블 스키마 초기화 설정.
 *
 * 애플리케이션 시작 시 정의된 테이블이 DB에 존재하지 않으면 자동으로 생성합니다.
 * [DatabaseInitializer]보다 먼저 실행되도록 [Order]를 1로 설정합니다.
 */
@Component
@Order(1)
class ExposedConfig : ApplicationRunner {

    @Transactional
    override fun run(args: ApplicationArguments?) {
        SchemaUtils.createMissingTablesAndColumns(
            Stores,
            StoreImages,
            StoreBusinessHours,
            StorePrices
        )
    }
}
