package com.hyunjine.reborn.ui.my

/**
 * 내 정보 화면의 UI 상태 모델.
 * @param userName 사용자 이름
 * @param email 사용자 이메일
 * @param hasStore 업체 등록 여부
 * @param storeInfo 등록된 업체 정보 (hasStore가 true일 때만 사용)
 */
data class MyModel(
    val userName: String = "김철수",
    val email: String = "kimcs@example.com",
    val hasStore: Boolean = false,
    val storeInfo: MyStoreModel? = null
)

/**
 * 내 업체 정보 모델.
 * @param name 업체명
 * @param address 업체 주소
 * @param isVerified 인증 업체 여부
 * @param isOpen 영업 중 여부
 */
data class MyStoreModel(
    val name: String = "",
    val address: String = "",
    val isVerified: Boolean = false,
    val isOpen: Boolean = true
)
