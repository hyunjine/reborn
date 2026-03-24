package com.hyunjine.reborn.data

import kotlinx.serialization.Serializable

/**
 * API 공통 응답 래퍼.
 *
 * @param T 응답 데이터 타입
 * @param success 요청 성공 여부
 * @param data 응답 데이터 (실패 시 null)
 * @param message 에러 메시지 (성공 시 null)
 */
@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String? = null
) {
    companion object {
        fun <T> success(data: T): ApiResponse<T> = ApiResponse(
            success = true,
            data = data
        )

        fun <T> error(message: String): ApiResponse<T> = ApiResponse(
            success = false,
            message = message
        )
    }
}
