package com.hyunjine.reborn.common.util

// commonMain
interface ClipboardManager {
    fun copyToClipboard(text: String)
}

// ?„ìš”???°ë¼ expect ?¨ìˆ˜ë¡?? ì–¸
expect fun ClipboardManager(): ClipboardManager