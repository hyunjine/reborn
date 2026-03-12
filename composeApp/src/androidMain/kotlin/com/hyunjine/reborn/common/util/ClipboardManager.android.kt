package com.hyunjine.reborn.common.util

import android.content.ClipData
import android.content.Context
import org.koin.core.annotation.Single
import org.koin.core.annotation.Singleton
import org.koin.mp.KoinPlatformTools

actual fun ClipboardManager(): ClipboardManager {
    return KoinPlatformTools.defaultContext().get().get<ClipboardManager>()
}

// androidMain
@Single
class AndroidClipboardManager(private val context: Context) : ClipboardManager {
    override fun copyToClipboard(text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = ClipData.newPlainText("label", text)
        clipboard.setPrimaryClip(clip)
    }
}