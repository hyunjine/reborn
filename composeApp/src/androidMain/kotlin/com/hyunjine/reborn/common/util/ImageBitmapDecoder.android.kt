package com.hyunjine.reborn.common.util

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

/**
 * ByteArrayë¥?ImageBitmap?¼ë¡œ ?”ì½”?©í•©?ˆë‹¤.
 * Android?ì„œ??BitmapFactoryë¥??¬ìš©?©ë‹ˆ??
 * @return ?”ì½”?©ëœ ImageBitmap
 */
actual fun ByteArray.decodeToImageBitmap(): ImageBitmap {
    return BitmapFactory.decodeByteArray(this, 0, size).asImageBitmap()
}
