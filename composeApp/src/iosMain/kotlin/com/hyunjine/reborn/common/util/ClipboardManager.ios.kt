package com.hyunjine.reborn.common.util

actual fun ClipboardManager(): ClipboardManager {
    return IosClipboardManager()
}

import platform.UIKit.UIPasteboard

class IosClipboardManager : ClipboardManager {
    override fun copyToClipboard(text: String) {
        UIPasteboard.generalPasteboard.string = text
    }
}