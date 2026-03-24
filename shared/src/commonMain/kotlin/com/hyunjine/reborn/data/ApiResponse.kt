package com.hyunjine.reborn.data

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

/**
 * API 공통 응답 래퍼.
 *
 * @param T 응답 데이터 타입
 */
@Serializable
@Stable
sealed interface ApiResponse<out T> {
    @Serializable
    data object Loading: ApiResponse<Nothing>


    /**
     * 요청 성공 응답.
     *
     * @param data 응답 데이터
     */
    @Serializable
    data class Success<T>(val data: T) : ApiResponse<T>

    /**
     * 요청 실패 응답.
     *
     * @param message 에러 메시지
     */
    @Serializable
    data class Error(val message: String) : ApiResponse<Nothing>
}
