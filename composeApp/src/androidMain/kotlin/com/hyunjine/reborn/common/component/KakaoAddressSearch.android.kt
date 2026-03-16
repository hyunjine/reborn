package com.hyunjine.reborn.common.component

import android.annotation.SuppressLint
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

/**
 * Android 플랫폼 카카오 우편번호 검색 WebView.
 * 카카오 우편번호 API를 WebView에 임베드하여 주소 검색 UI를 표시합니다.
 * @param modifier Modifier
 * @param onAddressSelected 주소 선택 완료 콜백 (도로명 주소 반환)
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
actual fun KakaoAddressSearchWebView(
    modifier: Modifier,
    onAddressSelected: (address: String) -> Unit
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                webViewClient = WebViewClient()
                addJavascriptInterface(
                    AddressBridge(onAddressSelected),
                    "AndroidBridge"
                )
                loadDataWithBaseURL(
                    "https://t1.kakaocdn.net",
                    KAKAO_POSTCODE_HTML,
                    "text/html",
                    "UTF-8",
                    null
                )
            }
        }
    )
}

private class AddressBridge(
    private val onAddressSelected: (String) -> Unit
) {
    @JavascriptInterface
    fun onAddressSelected(address: String) {
        onAddressSelected.invoke(address)
    }
}

private val KAKAO_POSTCODE_HTML = """
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <script src="//t1.kakaocdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
</head>
<body style="margin:0;padding:0;">
    <script>
        new kakao.Postcode({
            oncomplete: function(data) {
                var address = data.roadAddress || data.jibunAddress;
                if (data.buildingName) {
                    address += ' (' + data.buildingName + ')';
                }
                AndroidBridge.onAddressSelected(address);
            }
        }).open();
    </script>
</body>
</html>
""".trimIndent()
