package com.hyunjine.reborn.common.util

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.isoDayNumber
import java.time.format.TextStyle
import java.util.Locale

actual val DayOfWeek.shortName: String
    // ?ˆë“œë¡œì´???œìŠ¤??ë¡œì??¼ì— ë§ì¶° ?ë™?¼ë¡œ ë³€??(?? Mon, ????
    get() =  java.time.DayOfWeek.of(this.isoDayNumber)
        .getDisplayName(TextStyle.SHORT, Locale.getDefault())

actual val DayOfWeek.fullName: String
    // ?ˆë“œë¡œì´???œìŠ¤??ë¡œì??¼ì— ë§ì¶° ?ë™?¼ë¡œ ë³€??(?? Mon, ????
    get() =  java.time.DayOfWeek.of(this.isoDayNumber)
        .getDisplayName(TextStyle.FULL, Locale.getDefault())