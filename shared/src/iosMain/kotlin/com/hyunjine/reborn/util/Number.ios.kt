package com.hyunjine.reborn.util

import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterDecimalStyle

actual fun Number.readable(): String {
    val formatter = NSNumberFormatter().apply {
        numberStyle = NSNumberFormatterDecimalStyle
        minimumFractionDigits = 0u
        maximumFractionDigits = 2u
    }
    val nsNumber = NSNumber(this.toDouble())
    return formatter.stringFromNumber(nsNumber) ?: this.toString()
}
