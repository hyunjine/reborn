package com.hyunjine.reborn.common.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSMutableURLRequest
import platform.Foundation.NSURL
import platform.WebKit.WKNavigation
import platform.WebKit.WKNavigationDelegateProtocol
import platform.WebKit.WKScriptMessage
import platform.WebKit.WKScriptMessageHandlerProtocol
import platform.WebKit.WKUserContentController
import platform.WebKit.WKWebView
import platform.WebKit.WKWebViewConfiguration
import platform.darwin.NSObject

/**
 * iOS ?�랫??카카???�편번호 검??WebView.
 * WKWebView�??�용?�여 카카???�편번호 API 검??UI�??�시?�니??
 * @param modifier Modifier
 * @param onAddressSelected 주소 ?�택 ?�료 콜백 (?�로�?주소 반환)
 */
@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun KakaoAddressSearchWebView(
    modifier: Modifier,
    onAddressSelected: (address: String) -> Unit
) {
    val messageHandler = remember {
        object : NSObject(), WKScriptMessageHandlerProtocol {
            override fun userContentController(
                userContentController: WKUserContentController,
                didReceiveScriptMessage: WKScriptMessage
            ) {
                val address = didReceiveScriptMessage.body as? String ?: return
                onAddressSelected(address)
            }
        }
    }

    UIKitView(
        modifier = modifier,
        factory = {
            val configuration = WKWebViewConfiguration().apply {
                userContentController.addScriptMessageHandler(messageHandler, name = "iOSBridge")
            }
            val webView = WKWebView(frame = kotlinx.cinterop.cValue { }, configuration = configuration)
            webView.apply {
                val html = KAKAO_POSTCODE_HTML_IOS
                loadHTMLString(html, baseURL = NSURL(string = "https://t1.kakaocdn.net"))
            }
            webView
        }
    )
}

private val KAKAO_POSTCODE_HTML_IOS = """
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
                window.webkit.messageHandlers.iOSBridge.postMessage(address);
            }
        }).open();
    </script>
</body>
</html>
""".trimIndent()
