package com.hyunjine.reborn

import android.content.Context
import android.os.Build
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()