package com.hyunjine.reborn.common.util

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingToolbarDefaults.animationSpec
import androidx.compose.material3.ripple
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.hyunjine.reborn.common.theme.color

fun Modifier.animClickable(
    shape: Shape = RoundedCornerShape(12.dp),
    onClick: () -> Unit
): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // animationSpec 적용
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f, // 0.95로 좀 더 강조 가능
        animationSpec = tween(
            durationMillis = 200, // 0.1초 동안 실행
            easing = FastOutSlowInEasing // 서서히 빨라지다 느려짐
        ),
        label = "scale"
    )

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clip(shape) // 굴곡 적용
        .clickable(
            interactionSource = interactionSource,
            indication = ripple(
                color = color.gray500,
                bounded = true     // 요소 영역 안에서만 퍼질지 여부
            ),
            onClick = onClick
        )
}