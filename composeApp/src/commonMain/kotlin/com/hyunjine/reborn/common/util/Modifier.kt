package com.hyunjine.reborn.common.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

fun Modifier.clickable(
    ripple: Boolean = false,
    onClick: () -> Unit
): Modifier = if (ripple) {
    clickable(
        onClick = onClick
    )
} else {
    composed {
        this.clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() },
            onClick = { onClick() }
        )
    }
}