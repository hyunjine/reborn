package com.hyunjine.reborn.common.component

import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hyunjine.reborn.common.theme.RebornTheme
import com.hyunjine.reborn.common.theme.color
import com.hyunjine.reborn.common.theme.typography
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * 휠 스타일 숫자 피커.
 * 스크롤을 통해 값을 선택할 수 있는 원형 피커입니다.
 * @param items 선택 가능한 항목 목록
 * @param selectedIndex 현재 선택된 항목의 인덱스
 * @param onSelectedChanged 선택 변경 콜백
 * @param modifier Modifier
 * @param visibleCount 화면에 보이는 항목 수 (홀수 권장)
 * @param itemHeight 각 항목의 높이
 * @param textStyle 텍스트 스타일
 * @param selectedTextStyle 선택된 항목의 텍스트 스타일
 */
@Composable
fun WheelPicker(
    items: List<String>,
    selectedIndex: Int,
    onSelectedChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
    visibleCount: Int = 5,
    itemHeight: Dp = 48.dp,
    textStyle: TextStyle = typography.headingMedium20.copy(color = color.gray400),
    selectedTextStyle: TextStyle = typography.headingBold24.copy(color = color.gray900)
) {
    val halfVisible = visibleCount / 2
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = selectedIndex)

    val currentIndex by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val center = layoutInfo.viewportStartOffset +
                    (layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset) / 2
            layoutInfo.visibleItemsInfo.minByOrNull {
                val itemCenter = it.offset + it.size / 2
                kotlin.math.abs(itemCenter - center)
            }?.index?.minus(halfVisible)?.coerceIn(0, items.lastIndex) ?: selectedIndex
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { currentIndex }
            .distinctUntilChanged()
            .collect { onSelectedChanged(it) }
    }

    Box(
        modifier = modifier
            .height(itemHeight * visibleCount)
            .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
            .drawWithContent {
                drawContent()
                val fadeHeight = itemHeight.toPx() * halfVisible
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black),
                        startY = 0f,
                        endY = fadeHeight
                    ),
                    blendMode = BlendMode.DstIn
                )
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Black, Color.Transparent),
                        startY = size.height - fadeHeight,
                        endY = size.height
                    ),
                    blendMode = BlendMode.DstIn
                )
            }
    ) {
        LazyColumn(
            state = listState,
            flingBehavior = rememberSnapFlingBehavior(lazyListState = listState),
            modifier = Modifier.fillMaxWidth()
        ) {
            // 상단 패딩 아이템
            items(halfVisible) {
                Box(modifier = Modifier.height(itemHeight).fillMaxWidth())
            }

            items(items.size) { index ->
                Box(
                    modifier = Modifier
                        .height(itemHeight)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = items[index],
                        style = if (index == currentIndex) selectedTextStyle else textStyle
                    )
                }
            }

            // 하단 패딩 아이템
            items(halfVisible) {
                Box(modifier = Modifier.height(itemHeight).fillMaxWidth())
            }
        }
    }
}

/**
 * WheelPicker 미리보기.
 */
@Preview(showBackground = true)
@Composable
private fun WheelPickerPreview() {
    RebornTheme {
        WheelPicker(
            items = (0..23).map { it.toString().padStart(2, '0') },
            selectedIndex = 9,
            onSelectedChanged = {}
        )
    }
}
