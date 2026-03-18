package com.hyunjine.reborn.ui.my

import androidx.compose.runtime.Stable

/**
 * 내 정보 화면의 UI 상태 모델.
 * @param userName 사용자 이름.
 * @param email 사용자 이메일.
 * @param hasStore 업체 등록 여부.
 * @param storeInfo 등록된 업체 정보. [hasStore]가 true일 때만 유효합니다.
 */
@Stable
data class MyModel(
    val userName: String = "",
    val email: String = "",
    val hasStore: Boolean = false,
    val storeInfo: MyStoreModel? = null
)

/**
 * 등록된 업체 정보 모델.
 * @param name 업체명.
 * @param address 업체 주소.
 * @param imageUrl 업체 대표 이미지 URL.
 */
@Stable
data class MyStoreModel(
    val name: String = "",
    val address: String = "",
    val imageUrl: String = ""
)
