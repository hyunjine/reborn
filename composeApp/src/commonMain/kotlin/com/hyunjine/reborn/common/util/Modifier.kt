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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.hyunjine.reborn.common.theme.color

/**
 * Figma의 shadow strong 스타일을 적용합니다.
 * offset: (0, 3), blur: 4, color: black 15%
 * @param shape 그림자의 모양입니다.
 */
fun Modifier.shadowStrong(shape: Shape = RoundedCornerShape(0.dp)): Modifier =
    shadow(elevation = 4.dp, shape = shape)

/**
 * Figma의 shadow weak 스타일을 적용합니다.
 * offset: (0, 1), blur: 2, color: black 15%
 * @param shape 그림자의 모양입니다.
 */
fun Modifier.shadowWeak(shape: Shape = RoundedCornerShape(0.dp)): Modifier =
    shadow(elevation = 2.dp, shape = shape)

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