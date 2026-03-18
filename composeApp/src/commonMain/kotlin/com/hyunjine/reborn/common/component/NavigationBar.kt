package com.hyunjine.reborn.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hyunjine.reborn.common.theme.RebornTheme
import com.hyunjine.reborn.common.theme.color
import com.hyunjine.reborn.common.theme.typography
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import reborn.composeapp.generated.resources.Res
import reborn.composeapp.generated.resources.icon_24_home
import reborn.composeapp.generated.resources.icon_24_profile
import reborn.composeapp.generated.resources.icon_24_market_price

/**
 * 네비게이션 아이템에 대한 정보를 담는 열거형 클래스입니다.
 *
 * @property icon 아이템을 나타내는 아이콘 리소스
 * @property label 아이템의 하단에 표시될 텍스트
 */
enum class NavigationItem(
    val icon: DrawableResource,
    val label: String
) {
    Home(Res.drawable.icon_24_home, "홈"),
    MarketPrice(Res.drawable.icon_24_market_price, "시세"),
    MyInfo(Res.drawable.icon_24_profile, "내 정보")
}

/**
 * 앱의 하단 네비게이션 바 컴포넌트입니다.
 * 하단에 고정되어 주요 화면 간의 이동을 담당합니다.
 *
 * @param selectedItem 현재 선택된 네비게이션 아이템
 * @param onItemSelected 아이템이 클릭되었을 때 호출되는 콜백 함수
 * @param modifier 컴포넌트에 적용할 Modifier
 */
@Composable
fun NavigationBar(
    selectedItem: NavigationItem,
    onItemSelected: (NavigationItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color.white)
    ) {
        HorizontalDivider(
            thickness = 1.dp,
            color = color.gray200
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavigationItem.entries.forEach { item ->
                NavigationTab(
                    item = item,
                    isSelected = selectedItem == item,
                    onClick = { onItemSelected(item) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

/**
 * 네비게이션 바 내부의 개별 탭 컴포넌트입니다.
 * 아이콘과 라벨로 구성됩니다.
 *
 * @param item 표시할 네비게이션 아이템 정보
 * @param isSelected 탭이 선택된 상태인지 여부
 * @param onClick 탭이 클릭되었을 때 호출되는 콜백 함수
 * @param modifier 컴포넌트에 적용할 Modifier
 */
@Composable
private fun NavigationTab(
    item: NavigationItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val contentColor = if (isSelected) color.gray900 else color.gray400

    Column(
        modifier = modifier
            .fillMaxHeight()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(item.icon),
            contentDescription = item.label,
            modifier = Modifier.size(24.dp),
            tint = contentColor
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = item.label,
            style = typography.captionMedium12,
            color = contentColor
        )
    }
}

/**
 * [NavigationBar]의 디자인을 확인하기 위한 프리뷰입니다.
 */
@Preview(showBackground = true)
@Composable
private fun NavigationBarPreview() {
    RebornTheme {
        NavigationBar(
            selectedItem = NavigationItem.Home,
            onItemSelected = {}
        )
    }
}
