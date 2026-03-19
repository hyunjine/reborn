package com.hyunjine.reborn.common.util

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

expect val DayOfWeek.shortName: String

expect val DayOfWeek.fullName: String

fun LocalDateTime.Companion.now(): LocalDateTime {
    val tz = TimeZone.currentSystemDefault()
    return Clock.System.now().toLocalDateTime(tz)
}

fun LocalTime.Companion.now(): LocalTime {
    val tz = TimeZone.currentSystemDefault()
    return Clock.System.now().toLocalDateTime(tz).time
}