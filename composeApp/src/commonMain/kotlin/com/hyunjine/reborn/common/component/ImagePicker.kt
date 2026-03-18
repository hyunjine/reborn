package com.hyunjine.reborn.common.component

import androidx.compose.runtime.Composable

/**
 * 플랫폼별 이미지 피커를 실행하고 결과를 콜백으로 전달합니다.
 * @param maxSelection 최대 선택 가능 이미지 수
 * @param onResult 선택된 이미지의 ByteArray 목록 콜백
 * @param content 이미지 피커를 실행할 트리거를 포함하는 Composable (launcher 함수 전달)
 */
@Composable
expect fun ImagePickerLauncher(
    maxSelection: Int,
    onResult: (List<ByteArray>) -> Unit,
    content: @Composable (launch: () -> Unit) -> Unit
)
