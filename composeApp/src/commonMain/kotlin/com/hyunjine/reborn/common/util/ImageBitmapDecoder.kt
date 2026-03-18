package com.hyunjine.reborn.common.util

import androidx.compose.ui.graphics.ImageBitmap

/**
 * ByteArray를 ImageBitmap으로 디코딩합니다.
 * @return 디코딩된 ImageBitmap
 */
expect fun ByteArray.decodeToImageBitmap(): ImageBitmap
