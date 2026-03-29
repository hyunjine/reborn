package com.hyunjine.reborn.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hyunjine.reborn.common.theme.RebornTheme
import com.hyunjine.reborn.common.theme.color
import com.hyunjine.reborn.common.theme.typography
import com.hyunjine.reborn.common.util.animClickable

/**
 * Reborn 앱의 공통 버튼 컴포넌트입니다.
 * Box + background + clip + animClickable + Text 조합으로 구현되었습니다.
 * @param text 버튼에 표시할 텍스트입니다.
 * @param onClick 버튼 클릭 시 호출되는 콜백입니다.
 * @param modifier Modifier입니다.
 * @param type 버튼 크기 타입입니다. [RebornButtonType.Large] 또는 [RebornButtonType.Medium]을 지정합니다.
 */
@Composable
fun RebornButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    type: RebornButtonType = RebornButtonType.Large
) {
    val shape = RoundedCornerShape(14.dp)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(type.height)
            .clip(shape)
            .background(color.green500)
            .animClickable(shape = shape, onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = when (type) {
                RebornButtonType.Large -> typography.titleBold16
                RebornButtonType.Medium -> typography.bodyBold14
            },
            color = color.white,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}

/**
 * 버튼 크기 타입을 정의합니다.
 * @property height 버튼의 높이입니다.
 */
enum class RebornButtonType(val height: androidx.compose.ui.unit.Dp) {
    /** 높이 52dp의 큰 버튼입니다. */
    Large(52.dp),
    /** 높이 44dp의 중간 버튼입니다. */
    Medium(44.dp)
}

/**
 * RebornButton Large 타입 프리뷰입니다.
 */
@Preview(showBackground = true)
@Composable
private fun RebornButtonLargePreview() {
    RebornTheme {
        RebornButton(
            text = "문의하기",
            onClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

/**
 * RebornButton Medium 타입 프리뷰입니다.
 */
@Preview(showBackground = true)
@Composable
private fun RebornButtonMediumPreview() {
    RebornTheme {
        RebornButton(
            text = "문의하기",
            onClick = {},
            type = RebornButtonType.Medium,
            modifier = Modifier.padding(16.dp)
        )
    }
}
