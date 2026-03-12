package com.hyunjine.reborn.common.util

import java.text.DecimalFormat

actual fun Number.readable(): String {
    return DecimalFormat("#,###").format(this)
}