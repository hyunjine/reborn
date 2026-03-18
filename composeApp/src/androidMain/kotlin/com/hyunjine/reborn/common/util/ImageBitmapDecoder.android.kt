package com.hyunjine.reborn.common.util

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

/**
 * ByteArray를 ImageBitmap으로 디코딩합니다.
 * Android에서는 BitmapFactory를 사용합니다.
 * @return 디코딩된 ImageBitmap
 */
actual fun ByteArray.decodeToImageBitmap(): ImageBitmap {
    return BitmapFactory.decodeByteArray(this, 0, size).asImageBitmap()
}
