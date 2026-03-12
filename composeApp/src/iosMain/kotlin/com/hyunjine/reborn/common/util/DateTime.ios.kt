package com.hyunjine.reborn.common.util

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSCalendar
import platform.Foundation.NSDateComponents
import platform.Foundation.currentLocale
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.isoDayNumber
import platform.Foundation.NSLocale

actual val DayOfWeek.shortName: String
    get() {
    val formatter = NSDateFormatter().apply {
        locale = NSLocale.currentLocale
        dateFormat = "E" // "월", "Mon", "月" 등 (짧은 요일 형식)
        // 만약 "월요일" 처럼 길게 쓰고 싶다면 "EEEE"를 사용하세요.
    }

    // 2. ISO 요일 번호 (월=1, 일=7)를 iOS 요일 번호 (일=1, 월=2...토=7)로 변환
    // (isoDayNumber % 7) + 1 로직을 사용하면 월(1) -> 2, 일(7) -> 1 로 바뀝니다.
    val iosWeekday = (this.isoDayNumber % 7) + 1

    // 3. 특정 요일에 해당하는 NSDate 생성 (가장 정확한 방법은 weekdaySymbols 사용)
    // 하지만 가장 간단하고 확실한 방법은 formatter의 weekdaySymbols 배열을 직접 참조하는 것입니다.
    val symbols = formatter.shortWeekdaySymbols // ["Sun", "Mon", "Tue", ...]

    // symbols 배열은 일요일이 0번 인덱스입니다. (iosWeekday - 1)
    return symbols[(iosWeekday - 1)] as String
}

actual val DayOfWeek.fullName: String
    get() {
        val formatter = NSDateFormatter().apply {
            locale = NSLocale.currentLocale
            dateFormat = "EEE" // "월", "Mon", "月" 등 (짧은 요일 형식)
            // 만약 "월요일" 처럼 길게 쓰고 싶다면 "EEEE"를 사용하세요.
        }

        // 2. ISO 요일 번호 (월=1, 일=7)를 iOS 요일 번호 (일=1, 월=2...토=7)로 변환
        // (isoDayNumber % 7) + 1 로직을 사용하면 월(1) -> 2, 일(7) -> 1 로 바뀝니다.
        val iosWeekday = (this.isoDayNumber % 7) + 1

        // 3. 특정 요일에 해당하는 NSDate 생성 (가장 정확한 방법은 weekdaySymbols 사용)
        // 하지만 가장 간단하고 확실한 방법은 formatter의 weekdaySymbols 배열을 직접 참조하는 것입니다.
        val symbols = formatter.shortWeekdaySymbols // ["Sun", "Mon", "Tue", ...]

        // symbols 배열은 일요일이 0번 인덱스입니다. (iosWeekday - 1)
        return symbols[(iosWeekday - 1)] as String
    }