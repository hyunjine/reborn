package com.hyunjine.reborn.common.util

import androidx.compose.ui.graphics.ImageBitmap

/**
 * ByteArrayë¥?ImageBitmap?¼ë¡œ ?”ì½”?©í•©?ˆë‹¤.
 * @return ?”ì½”?©ëœ ImageBitmap
 */
expect fun ByteArray.decodeToImageBitmap(): ImageBitmap
