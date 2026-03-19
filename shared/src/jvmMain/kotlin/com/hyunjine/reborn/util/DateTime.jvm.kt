package com.hyunjine.reborn.util

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.isoDayNumber
import java.time.format.TextStyle
import java.util.Locale

actual val DayOfWeek.shortName: String
    get() = java.time.DayOfWeek.of(this.isoDayNumber)
        .getDisplayName(TextStyle.SHORT, Locale.getDefault())

actual val DayOfWeek.fullName: String
    get() = java.time.DayOfWeek.of(this.isoDayNumber)
        .getDisplayName(TextStyle.FULL, Locale.getDefault())
