package com.hyunjine.reborn.common.util

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.isoDayNumber
import java.time.format.TextStyle
import java.util.Locale

actual val DayOfWeek.shortName: String
    // 안드로이드 시스템 로케일에 맞춰 자동으로 변환 (월, Mon, 月 등)
    get() =  java.time.DayOfWeek.of(this.isoDayNumber)
        .getDisplayName(TextStyle.SHORT, Locale.getDefault())

actual val DayOfWeek.fullName: String
    // 안드로이드 시스템 로케일에 맞춰 자동으로 변환 (월, Mon, 月 등)
    get() =  java.time.DayOfWeek.of(this.isoDayNumber)
        .getDisplayName(TextStyle.FULL, Locale.getDefault())