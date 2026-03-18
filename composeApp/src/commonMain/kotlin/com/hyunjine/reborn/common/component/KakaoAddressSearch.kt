package com.hyunjine.reborn.common.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * 카카오 우편번호 API를 이용한 주소 검색 WebView.
 * 플랫폼별로 WebView를 통해 카카오 우편번호 검색 UI를 표시합니다.
 * @param modifier Modifier
 * @param onAddressSelected 주소 선택 완료 콜백 (도로명 주소 반환)
 */
@Composable
expect fun KakaoAddressSearchWebView(
    modifier: Modifier = Modifier,
    onAddressSelected: (address: String) -> Unit
)
