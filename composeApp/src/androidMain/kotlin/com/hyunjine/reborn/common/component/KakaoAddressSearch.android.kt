package com.hyunjine.reborn.common.component

import android.annotation.SuppressLint
import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

private const val TAG = "KakaoAddress"

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
                settings.javaScriptCanOpenWindowsAutomatically = true
                settings.setSupportMultipleWindows(true)
                settings.useWideViewPort = true
                settings.loadWithOverviewMode = true

                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        Log.d(TAG, "onPageFinished: $url")
                    }

                    override fun onReceivedError(
                        view: WebView?,
                        request: WebResourceRequest?,
                        error: WebResourceError?
                    ) {
                        super.onReceivedError(view, request, error)
                        Log.e(TAG, "onReceivedError: ${request?.url} / ${error?.description}")
                    }
                }

                webChromeClient = object : WebChromeClient() {
                    override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                        Log.d(TAG, "JS Console: ${consoleMessage?.message()}")
                        return true
                    }
                }

                addJavascriptInterface(
                    KakaoAddressBridge(onAddressSelected),
                    "Android"
                )

                Log.d(TAG, "Loading Kakao Postcode HTML")
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

class KakaoAddressBridge(
    private val onAddressSelected: (String) -> Unit
) {
    @JavascriptInterface
    fun processAddress(address: String) {
        Log.d(TAG, "processAddress called: $address")
        onAddressSelected(address)
    }
}

private val KAKAO_POSTCODE_HTML = """
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <script src="https://t1.kakaocdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
</head>
<body style="margin:0;padding:0;">
    <div id="postcode-container" style="width:100%;height:100vh;"></div>
    <script>
        console.log('Kakao Postcode script loaded, starting embed...');
        new kakao.Postcode({
            oncomplete: function(data) {
                console.log('Address selected: ' + data.roadAddress);
                Android.processAddress(data.roadAddress);
            },
            width: '100%',
            height: '100%'
        }).embed(document.getElementById('postcode-container'));
        console.log('Kakao Postcode embed called');
    </script>
</body>
</html>
""".trimIndent()
