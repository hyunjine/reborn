package com.hyunjine.reborn.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hyunjine.reborn.common.theme.RebornTheme
import com.hyunjine.reborn.common.theme.color
import com.hyunjine.reborn.common.theme.typography
import com.hyunjine.reborn.common.util.animClickable
import com.hyunjine.reborn.common.util.shadowStrong
import org.jetbrains.compose.resources.painterResource
import reborn.composeapp.generated.resources.Res
import reborn.composeapp.generated.resources.icon_24_bell
import reborn.composeapp.generated.resources.icon_24_search

/**
 * 홈 화면의 상단 앱바 컴포넌트입니다.
 * 검색 필드와 알림 아이콘으로 구성됩니다.
 *
 * @param onSearchClick 검색 영역 클릭 시 호출되는 콜백입니다.
 * @param onNotificationClick 알림 아이콘 클릭 시 호출되는 콜백입니다.
 * @param modifier Modifier입니다.
 * @param style 앱바 스타일입니다. [HomeAppBarStyle.Flat] 또는 [HomeAppBarStyle.Float]을 지정합니다.
 * @param showDivider 하단 구분선 표시 여부입니다. [HomeAppBarStyle.Flat]에서만 적용됩니다.
 */
@Composable
fun HomeAppBar(
    onSearchClick: () -> Unit,
    onNotificationClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: HomeAppBarStyle = HomeAppBarStyle.Flat,
    showDivider: Boolean = false
) {
    val borderColor = color.gray200
    val shape = if (style == HomeAppBarStyle.Float) RoundedCornerShape(16.dp) else RoundedCornerShape(0.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (style == HomeAppBarStyle.Float) {
                    Modifier
                        .padding(horizontal = 8.dp)
                        .shadowStrong(shape = shape)
                } else {
                    Modifier
                }
            )
            .then(
                if (style == HomeAppBarStyle.Flat) Modifier.statusBarsPadding() else Modifier
            )
            .clip(shape)
            .background(color.white)
            .height(56.dp)
            .then(
                if (style == HomeAppBarStyle.Flat && showDivider) {
                    Modifier.drawBehind {
                        val strokeWidth = 1.dp.toPx()
                        drawLine(
                            color = borderColor,
                            start = Offset(0f, size.height - strokeWidth / 2),
                            end = Offset(size.width, size.height - strokeWidth / 2),
                            strokeWidth = strokeWidth
                        )
                    }
                } else Modifier
            )
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .animClickable(onClick = onSearchClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.icon_24_search),
                contentDescription = "검색",
                modifier = Modifier.size(24.dp),
                tint = color.gray900
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .height(44.dp)
                .clip(RoundedCornerShape(99.dp))
                .background(color.gray50)
                .animClickable(shape = RoundedCornerShape(99.dp), onClick = onSearchClick)
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "검색하기",
                style = typography.bodyMedium16,
                color = color.gray800,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Box(
            modifier = Modifier
                .size(48.dp)
                .animClickable(onClick = onNotificationClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.icon_24_bell),
                contentDescription = "알림",
                modifier = Modifier.size(24.dp),
                tint = color.gray900
            )
        }
    }
}

/**
 * HomeAppBar의 스타일을 정의합니다.
 */
enum class HomeAppBarStyle {
    /** 일반 플랫 스타일입니다. 리스트 뷰에서 사용됩니다. */
    Flat,
    /** 플로팅 스타일입니다. 지도 뷰에서 사용됩니다. 라운드 + 그림자가 적용됩니다. */
    Float
}

/**
 * HomeAppBar Flat 스타일 프리뷰입니다.
 */
@Preview(showBackground = true)
@Composable
private fun HomeAppBarFlatPreview() {
    RebornTheme {
        HomeAppBar(
            onSearchClick = {},
            onNotificationClick = {},
            style = HomeAppBarStyle.Flat,
            showDivider = true
        )
    }
}

/**
 * HomeAppBar Float 스타일 프리뷰입니다.
 */
@Preview(showBackground = true)
@Composable
private fun HomeAppBarFloatPreview() {
    RebornTheme {
        HomeAppBar(
            onSearchClick = {},
            onNotificationClick = {},
            style = HomeAppBarStyle.Float
        )
    }
}
