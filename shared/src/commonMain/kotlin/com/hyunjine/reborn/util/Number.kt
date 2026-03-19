package com.hyunjine.reborn.util

// commonMain
expect fun Number.readable(): String

fun Int.pad(): String = this.toString().padStart(2, '0')