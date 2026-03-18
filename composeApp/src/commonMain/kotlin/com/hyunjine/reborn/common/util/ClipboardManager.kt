package com.hyunjine.reborn.common.util

// commonMain
interface ClipboardManager {
    fun copyToClipboard(text: String)
}

// 필요에 따라 expect 함수로 선언
expect fun ClipboardManager(): ClipboardManager