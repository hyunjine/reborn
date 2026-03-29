package com.hyunjine.reborn.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hyunjine.reborn.common.theme.RebornTheme
import com.hyunjine.reborn.common.theme.color
import com.hyunjine.reborn.common.util.animClickable
import com.hyunjine.reborn.common.util.shadowStrong
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import reborn.composeapp.generated.resources.Res
import reborn.composeapp.generated.resources.icon_24_map

/**
 * Reborn 앱의 공통 플로팅 액션 버튼입니다.
 * 56dp 크기의 녹색 원형 버튼으로, 아이콘을 표시합니다.
 *
 * @param icon 버튼에 표시할 아이콘 리소스입니다.
 * @param contentDescription 접근성을 위한 콘텐츠 설명입니다.
 * @param onClick 버튼 클릭 시 호출되는 콜백입니다.
 * @param modifier Modifier입니다.
 */
@Composable
fun FloatButton(
    icon: DrawableResource,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(56.dp)
            .shadowStrong(shape = CircleShape)
            .animClickable(shape = CircleShape, onClick = onClick)
            .background(color = color.green500, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = contentDescription,
            tint = color.white,
            modifier = Modifier.size(24.dp)
        )
    }
}

/**
 * FloatButton 프리뷰입니다.
 */
@Preview(showBackground = true)
@Composable
private fun FloatButtonPreview() {
    RebornTheme {
        FloatButton(
            icon = Res.drawable.icon_24_map,
            contentDescription = "지도 보기",
            onClick = {}
        )
    }
}
