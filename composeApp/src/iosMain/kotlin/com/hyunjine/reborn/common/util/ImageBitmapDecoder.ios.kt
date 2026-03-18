package com.hyunjine.reborn.common.util

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image

/**
 * ByteArrayë¥?ImageBitmap?¼ë¡œ ?”ì½”?©í•©?ˆë‹¤.
 * iOS?ì„œ??Skia??Imageë¥??¬ìš©?©ë‹ˆ??
 * @return ?”ì½”?©ëœ ImageBitmap
 */
actual fun ByteArray.decodeToImageBitmap(): ImageBitmap {
    return Image.makeFromEncoded(this).toComposeImageBitmap()
}
