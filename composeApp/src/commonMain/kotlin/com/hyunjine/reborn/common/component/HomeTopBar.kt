package com.hyunjine.reborn.common.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hyunjine.reborn.common.theme.RebornTheme
import com.hyunjine.reborn.common.theme.color
import com.hyunjine.reborn.common.theme.typography
import com.hyunjine.reborn.common.util.animClickable
import org.jetbrains.compose.resources.painterResource
import reborn.composeapp.generated.resources.Res
import reborn.composeapp.generated.resources.icon_24_arrow_right
import reborn.composeapp.generated.resources.icon_24_bell
import reborn.composeapp.generated.resources.icon_24_location
import reborn.composeapp.generated.resources.icon_24_search

/**
 * 홈 화면의 상단 앱바입니다.
 * 위치 정보와 검색/알림 아이콘을 표시합니다.
 * @param isScrolled 스크롤 여부에 따라 하단 구분선을 표시합니다.
 * @param onLocationClick 위치 영역 클릭 시 호출되는 콜백입니다.
 * @param onSearchClick 검색 아이콘 클릭 시 호출되는 콜백입니다.
 * @param onNotificationClick 알림 아이콘 클릭 시 호출되는 콜백입니다.
 * @param modifier Modifier입니다.
 */
@Composable
fun HomeTopBar(
    isScrolled: Boolean,
    onLocationClick: () -> Unit,
    onSearchClick: () -> Unit,
    onNotificationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = color.gray200
    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(54.dp)
            .then(
                if (isScrolled) Modifier.drawBehind {
                    val strokeWidth = 1.dp.toPx()
                    drawLine(
                        color = borderColor,
                        start = Offset(0f, size.height - strokeWidth / 2),
                        end = Offset(size.width, size.height - strokeWidth / 2),
                        strokeWidth = strokeWidth
                    )
                } else Modifier
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(weight = 1F, fill = false)
                .animClickable(onClick = onLocationClick)
                .padding(vertical = 15.dp),
        ) {
            Icon(
                painter = painterResource(Res.drawable.icon_24_location),
                contentDescription = null,
                tint = color.gray900,
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = "내 위치",
                modifier = Modifier.weight(1f, fill = false),
                style = typography.titleSemibold16,
                color = color.gray900,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Icon(
                painter = painterResource(Res.drawable.icon_24_arrow_right),
                contentDescription = null,
                tint = color.gray500,
                modifier = Modifier.size(18.dp)
            )
        }

        Row {
            Box(
                modifier = Modifier
                    .size(40.dp)
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
                    .size(40.dp)
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
}

/**
 * HomeTopBar 기본 상태 프리뷰입니다.
 */
@Preview(showBackground = true)
@Composable
private fun HomeTopBarPreview() {
    RebornTheme {
        HomeTopBar(
            isScrolled = false,
            onLocationClick = {},
            onSearchClick = {},
            onNotificationClick = {}
        )
    }
}

/**
 * HomeTopBar 스크롤 상태 프리뷰입니다.
 */
@Preview(showBackground = true)
@Composable
private fun HomeTopBarScrolledPreview() {
    RebornTheme {
        HomeTopBar(
            isScrolled = true,
            onLocationClick = {},
            onSearchClick = {},
            onNotificationClick = {}
        )
    }
}
