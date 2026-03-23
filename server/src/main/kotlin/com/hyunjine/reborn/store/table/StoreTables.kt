package com.hyunjine.reborn.store.table

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.jetbrains.exposed.sql.kotlin.datetime.time

object Stores : LongIdTable("stores") {
    val name = varchar("name", 100)
    val address = varchar("address", 255)
    val description = text("description")
    val phoneNumber = varchar("phone_number", 20)
    val lastUpdated = datetime("last_updated")
}

object StoreImages : LongIdTable("store_images") {
    val storeId = reference("store_id", Stores)
    val imageUrl = varchar("image_url", 500)
}

object StoreBusinessHours : LongIdTable("store_business_hours") {
    val storeId = reference("store_id", Stores)
    val dayOfWeek = varchar("day_of_week", 10)
    val isOpen = bool("is_open")
    val openTime = time("open_time").nullable()
    val closeTime = time("close_time").nullable()
}

object StorePrices : LongIdTable("store_prices") {
    val storeId = reference("store_id", Stores)
    val name = varchar("name", 50)
    val price = varchar("price", 50)
}
