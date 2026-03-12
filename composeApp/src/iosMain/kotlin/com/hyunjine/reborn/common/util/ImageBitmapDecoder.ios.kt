package com.hyunjine.reborn.common.util

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image

/**
 * ByteArray를 ImageBitmap으로 디코딩합니다.
 * iOS에서는 Skia의 Image를 사용합니다.
 * @return 디코딩된 ImageBitmap
 */
actual fun ByteArray.decodeToImageBitmap(): ImageBitmap {
    return Image.makeFromEncoded(this).toComposeImageBitmap()
}
