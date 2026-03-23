package com.hyunjine.reborn.store.table

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.jetbrains.exposed.sql.kotlin.datetime.time

/**
 * 업체 기본 정보 테이블.
 *
 * 업체명, 주소, 소개, 전화번호, 시세 최종 업데이트 시각을 저장합니다.
 */
object Stores : LongIdTable("stores") {
    /** 업체명 */
    val name = varchar("name", 100)
    /** 업체 주소 */
    val address = varchar("address", 255)
    /** 업체 소개 텍스트 */
    val description = text("description")
    /** 업체 전화번호 */
    val phoneNumber = varchar("phone_number", 20)
    /** 시세 최종 업데이트 시각 */
    val lastUpdated = datetime("last_updated")
}

/**
 * 업체 이미지 테이블.
 *
 * 하나의 업체에 여러 이미지를 1:N 관계로 저장합니다.
 */
object StoreImages : LongIdTable("store_images") {
    /** 소속 업체 외래키 */
    val storeId = reference("store_id", Stores)
    /** 이미지 URL */
    val imageUrl = varchar("image_url", 500)
}

/**
 * 업체 영업시간 테이블.
 *
 * 요일별 영업 여부와 영업 시작/종료 시각을 저장합니다.
 * 휴무일인 경우 [openTime], [closeTime]은 null입니다.
 */
object StoreBusinessHours : LongIdTable("store_business_hours") {
    /** 소속 업체 외래키 */
    val storeId = reference("store_id", Stores)
    /** 요일 (DayOfWeek.name 형식, e.g. "MONDAY") */
    val dayOfWeek = varchar("day_of_week", 10)
    /** 해당 요일 영업 여부 */
    val isOpen = bool("is_open")
    /** 영업 시작 시각 (휴무일이면 null) */
    val openTime = time("open_time").nullable()
    /** 영업 종료 시각 (휴무일이면 null) */
    val closeTime = time("close_time").nullable()
}

/**
 * 업체 매입 시세 테이블.
 *
 * 품목별 단가 정보를 저장합니다.
 */
object StorePrices : LongIdTable("store_prices") {
    /** 소속 업체 외래키 */
    val storeId = reference("store_id", Stores)
    /** 품목명 (e.g. "고철", "알루미늄") */
    val name = varchar("name", 50)
    /** 단가 텍스트 (e.g. "450원/kg") */
    val price = varchar("price", 50)
}
