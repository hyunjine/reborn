package com.hyunjine.reborn.config

import com.hyunjine.reborn.store.table.StoreBusinessHours
import com.hyunjine.reborn.store.table.StoreImages
import com.hyunjine.reborn.store.table.StorePrices
import com.hyunjine.reborn.store.table.Stores
import org.jetbrains.exposed.sql.SchemaUtils
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

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
