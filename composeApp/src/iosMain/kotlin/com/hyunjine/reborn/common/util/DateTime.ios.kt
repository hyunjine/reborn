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
        dateFormat = "E" // "??, "Mon", "?? ??(ì§§ì? ?”ì¼ ?•ì‹)
        // ë§Œì•½ "?”ìš”?? ì²˜ëŸ¼ ê¸¸ê²Œ ?°ê³  ?¶ë‹¤ë©?"EEEE"ë¥??¬ìš©?˜ì„¸??
    }

    // 2. ISO ?”ì¼ ë²ˆí˜¸ (??1, ??7)ë¥?iOS ?”ì¼ ë²ˆí˜¸ (??1, ??2...??7)ë¡?ë³€??
    // (isoDayNumber % 7) + 1 ë¡œì§???¬ìš©?˜ë©´ ??1) -> 2, ??7) -> 1 ë¡?ë°”ë€ë‹ˆ??
    val iosWeekday = (this.isoDayNumber % 7) + 1

    // 3. ?¹ì • ?”ì¼???´ë‹¹?˜ëŠ” NSDate ?ì„± (ê°€???•í™•??ë°©ë²•?€ weekdaySymbols ?¬ìš©)
    // ?˜ì?ë§?ê°€??ê°„ë‹¨?˜ê³  ?•ì‹¤??ë°©ë²•?€ formatter??weekdaySymbols ë°°ì—´??ì§ì ‘ ì°¸ì¡°?˜ëŠ” ê²ƒì…?ˆë‹¤.
    val symbols = formatter.shortWeekdaySymbols // ["Sun", "Mon", "Tue", ...]

    // symbols ë°°ì—´?€ ?¼ìš”?¼ì´ 0ë²??¸ë±?¤ì…?ˆë‹¤. (iosWeekday - 1)
    return symbols[(iosWeekday - 1)] as String
}

actual val DayOfWeek.fullName: String
    get() {
        val formatter = NSDateFormatter().apply {
            locale = NSLocale.currentLocale
            dateFormat = "EEE" // "??, "Mon", "?? ??(ì§§ì? ?”ì¼ ?•ì‹)
            // ë§Œì•½ "?”ìš”?? ì²˜ëŸ¼ ê¸¸ê²Œ ?°ê³  ?¶ë‹¤ë©?"EEEE"ë¥??¬ìš©?˜ì„¸??
        }

        // 2. ISO ?”ì¼ ë²ˆí˜¸ (??1, ??7)ë¥?iOS ?”ì¼ ë²ˆí˜¸ (??1, ??2...??7)ë¡?ë³€??
        // (isoDayNumber % 7) + 1 ë¡œì§???¬ìš©?˜ë©´ ??1) -> 2, ??7) -> 1 ë¡?ë°”ë€ë‹ˆ??
        val iosWeekday = (this.isoDayNumber % 7) + 1

        // 3. ?¹ì • ?”ì¼???´ë‹¹?˜ëŠ” NSDate ?ì„± (ê°€???•í™•??ë°©ë²•?€ weekdaySymbols ?¬ìš©)
        // ?˜ì?ë§?ê°€??ê°„ë‹¨?˜ê³  ?•ì‹¤??ë°©ë²•?€ formatter??weekdaySymbols ë°°ì—´??ì§ì ‘ ì°¸ì¡°?˜ëŠ” ê²ƒì…?ˆë‹¤.
        val symbols = formatter.shortWeekdaySymbols // ["Sun", "Mon", "Tue", ...]

        // symbols ë°°ì—´?€ ?¼ìš”?¼ì´ 0ë²??¸ë±?¤ì…?ˆë‹¤. (iosWeekday - 1)
        return symbols[(iosWeekday - 1)] as String
    }