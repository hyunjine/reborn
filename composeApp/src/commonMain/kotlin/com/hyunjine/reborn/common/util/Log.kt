package com.hyunjine.reborn.common.util

import io.github.aakira.napier.Napier

fun log(vararg message: Any?) {
    Napier.d(message.joinToString("\n"), tag = "winter")
}
