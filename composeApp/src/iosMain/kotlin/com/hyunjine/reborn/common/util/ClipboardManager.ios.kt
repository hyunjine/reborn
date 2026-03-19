package com.hyunjine.reborn.common.util

import platform.UIKit.UIPasteboard

actual fun ClipboardManager(): ClipboardManager {
    return IosClipboardManager()
}

class IosClipboardManager : ClipboardManager {
    override fun copyToClipboard(text: String) {
        UIPasteboard.generalPasteboard.string = text
    }
}