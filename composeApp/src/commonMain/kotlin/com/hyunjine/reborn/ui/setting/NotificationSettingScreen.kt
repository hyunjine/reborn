package com.hyunjine.reborn.ui.setting

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
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import com.hyunjine.reborn.common.theme.RebornTheme
import com.hyunjine.reborn.common.theme.color
import com.hyunjine.reborn.common.theme.typography
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import reborn.composeapp.generated.resources.Res
import reborn.composeapp.generated.resources.icon_24_arrow_left

/**
 * 알림 설정 화면.
 * 알림 수신 여부를 토글할 수 있는 스위치를 제공합니다.
 */
@Serializable
object NotificationSettingScreen : NavKey {

    /**
     * 알림 설정 화면에서 발생하는 UI 이벤트들입니다.
     */
    sealed interface UiEvent {
        /**
         * 알림 토글 변경 시 발생하는 이벤트입니다.
         * @param enabled 알림 활성화 여부.
         */
        data class NotificationToggled(val enabled: Boolean) : UiEvent
    }

    /**
     * 알림 설정 화면의 Stateful Wrapper입니다.
     * @param viewModel Koin을 통해 주입되는 ViewModel입니다.
     * @param onBack 뒤로가기 콜백입니다.
     */
    @Composable
    operator fun invoke(
        viewModel: SettingViewModel = koinViewModel(),
        onBack: () -> Unit = {}
    ) {
        val isNotificationEnabled by viewModel.isNotificationEnabled.collectAsStateWithLifecycle()
        invoke(
            isNotificationEnabled = isNotificationEnabled,
            onEvent = { viewModel.event(it) },
            onBack = onBack
        )
    }

    /**
     * 알림 설정 화면의 Stateless UI 구현체입니다.
     * @param isNotificationEnabled 현재 알림 활성화 상태입니다.
     * @param onEvent UI 이벤트 처리를 위한 콜백입니다.
     * @param onBack 뒤로가기 콜백입니다.
     */
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    operator fun invoke(
        isNotificationEnabled: Boolean,
        onEvent: (UiEvent) -> Unit = {},
        onBack: () -> Unit = {}
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "알림 설정",
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
                NotificationToggleRow(
                    isEnabled = isNotificationEnabled,
                    onToggle = { onEvent(UiEvent.NotificationToggled(it)) }
                )
            }
        }
    }
}

/**
 * 알림 설정 토글 행입니다.
 * 좌측에 "알림 설정" 텍스트, 우측에 스위치를 표시합니다.
 * @param isEnabled 현재 알림 활성화 상태입니다.
 * @param onToggle 스위치 변경 시 호출되는 콜백입니다.
 */
@Composable
private fun NotificationToggleRow(
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "알림 설정",
            style = typography.bodyMedium16,
            color = color.gray900,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = isEnabled,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = color.green500,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = color.gray300,
                uncheckedBorderColor = Color.Transparent,
                checkedBorderColor = Color.Transparent
            )
        )
    }
}

/**
 * 알림 설정 화면 프리뷰 (알림 활성화 상태).
 */
@Preview(showBackground = true)
@Composable
private fun NotificationSettingScreenOnPreview() {
    RebornTheme {
        NotificationSettingScreen(
            isNotificationEnabled = true
        )
    }
}

/**
 * 알림 설정 화면 프리뷰 (알림 비활성화 상태).
 */
@Preview(showBackground = true)
@Composable
private fun NotificationSettingScreenOffPreview() {
    RebornTheme {
        NotificationSettingScreen(
            isNotificationEnabled = false
        )
    }
}
