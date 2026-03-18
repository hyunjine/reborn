package com.hyunjine.reborn.ui.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.hyunjine.reborn.common.theme.RebornTheme
import com.hyunjine.reborn.common.theme.color
import com.hyunjine.reborn.common.theme.typography
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import reborn.composeapp.generated.resources.Res
import reborn.composeapp.generated.resources.icon_24_arrow_left
import reborn.composeapp.generated.resources.icon_24_arrow_right
import reborn.composeapp.generated.resources.icon_24_bell
import reborn.composeapp.generated.resources.icon_24_logout
import reborn.composeapp.generated.resources.icon_24_stop

/**
 * 설정 화면.
 * 알림 설정, 로그아웃, 계정 탈퇴 메뉴를 제공합니다.
 */
@Serializable
object SettingScreen : NavKey {

    /**
     * 설정 화면에서 발생하는 UI 이벤트들입니다.
     */
    sealed interface UiEvent {
        /**
         * 알림 설정 메뉴 클릭 시 발생하는 이벤트입니다.
         */
        data object NotificationSettingClicked : UiEvent

        /**
         * 로그아웃 메뉴 클릭 시 발생하는 이벤트입니다.
         */
        data object LogoutClicked : UiEvent

        /**
         * 계정 탈퇴 메뉴 클릭 시 발생하는 이벤트입니다.
         */
        data object DeleteAccountClicked : UiEvent
    }

    /**
     * 설정 화면의 Stateful Wrapper입니다.
     * @param onBack 뒤로가기 콜백입니다.
     * @param onNotificationSetting 알림 설정 화면으로 이동하는 콜백입니다.
     */
    @Composable
    operator fun invoke(
        onBack: () -> Unit = {},
        onNotificationSetting: () -> Unit = {}
    ) {
        invoke(
            onEvent = { event ->
                when (event) {
                    is UiEvent.NotificationSettingClicked -> onNotificationSetting()
                    is UiEvent.LogoutClicked -> { /* TODO: 로그아웃 처리 */ }
                    is UiEvent.DeleteAccountClicked -> { /* TODO: 계정 탈퇴 처리 */ }
                }
            },
            onBack = onBack
        )
    }

    /**
     * 설정 화면의 Stateless UI 구현체입니다.
     * @param onEvent UI 이벤트 처리를 위한 콜백입니다.
     * @param onBack 뒤로가기 콜백입니다.
     */
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    operator fun invoke(
        onEvent: (UiEvent) -> Unit = {},
        onBack: () -> Unit = {}
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "설정",
                            style = typography.headingMedium18
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                painter = painterResource(Res.drawable.icon_24_arrow_left),
                                contentDescription = "뒤로가기",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White
                    )
                )
            },
            containerColor = Color.White
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp)
            ) {
                SettingMenuItem(
                    icon = {
                        Icon(
                            painter = painterResource(Res.drawable.icon_24_bell),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = color.gray900
                        )
                    },
                    title = "알림 설정",
                    onClick = { onEvent(UiEvent.NotificationSettingClicked) }
                )
                SettingMenuItem(
                    icon = {
                        Icon(
                            painter = painterResource(Res.drawable.icon_24_logout),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = color.gray700
                        )
                    },
                    title = "로그아웃",
                    onClick = { onEvent(UiEvent.LogoutClicked) }
                )
                SettingMenuItem(
                    icon = {
                        Icon(
                            painter = painterResource(Res.drawable.icon_24_stop),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = color.gray700
                        )
                    },
                    title = "계정 탈퇴",
                    onClick = { onEvent(UiEvent.DeleteAccountClicked) }
                )
            }
        }
    }
}

/**
 * 설정 화면의 메뉴 아이템입니다.
 * @param icon 좌측 아이콘 Composable.
 * @param title 메뉴 제목.
 * @param onClick 메뉴 클릭 시 호출되는 콜백입니다.
 */
@Composable
private fun SettingMenuItem(
    icon: @Composable () -> Unit,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        icon()
        Text(
            text = title,
            style = typography.bodyMedium16,
            color = color.gray900,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Icon(
            painter = painterResource(Res.drawable.icon_24_arrow_right),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = color.gray600
        )
    }
}

/**
 * 설정 화면 프리뷰입니다.
 */
@Preview(showBackground = true)
@Composable
private fun SettingScreenPreview() {
    RebornTheme {
        SettingScreen(
            onEvent = {},
            onBack = {}
        )
    }
}
