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
import reborn.composeapp.generated.resources.icon_24_bell
import reborn.composeapp.generated.resources.icon_24_arrow_right
import reborn.composeapp.generated.resources.icon_24_logout
import reborn.composeapp.generated.resources.icon_24_stop

/**
 * ?¤ì • ?”ë©´.
 * ?Œë¦¼ ?¤ì •, ë¡œê·¸?„ì›ƒ, ê³„ì • ?ˆí‡´ ë©”ë‰´ë¥??œê³µ?©ë‹ˆ??
 */
@Serializable
object SettingScreen : NavKey {

    /**
     * ?¤ì • ?”ë©´?ì„œ ë°œìƒ?˜ëŠ” UI ?´ë²¤?¸ë“¤?…ë‹ˆ??
     */
    sealed interface UiEvent {
        /**
         * ?Œë¦¼ ?¤ì • ë©”ë‰´ ?´ë¦­ ??ë°œìƒ?˜ëŠ” ?´ë²¤?¸ìž…?ˆë‹¤.
         */
        data object NotificationSettingClicked : UiEvent

        /**
         * ë¡œê·¸?„ì›ƒ ë©”ë‰´ ?´ë¦­ ??ë°œìƒ?˜ëŠ” ?´ë²¤?¸ìž…?ˆë‹¤.
         */
        data object LogoutClicked : UiEvent

        /**
         * ê³„ì • ?ˆí‡´ ë©”ë‰´ ?´ë¦­ ??ë°œìƒ?˜ëŠ” ?´ë²¤?¸ìž…?ˆë‹¤.
         */
        data object DeleteAccountClicked : UiEvent
    }

    /**
     * ?¤ì • ?”ë©´??Stateful Wrapper?…ë‹ˆ??
     * @param onBack ?¤ë¡œê°€ê¸?ì½œë°±?…ë‹ˆ??
     * @param onNotificationSetting ?Œë¦¼ ?¤ì • ?”ë©´?¼ë¡œ ?´ë™?˜ëŠ” ì½œë°±?…ë‹ˆ??
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
                    is UiEvent.LogoutClicked -> { /* TODO: ë¡œê·¸?„ì›ƒ ì²˜ë¦¬ */ }
                    is UiEvent.DeleteAccountClicked -> { /* TODO: ê³„ì • ?ˆí‡´ ì²˜ë¦¬ */ }
                }
            },
            onBack = onBack
        )
    }

    /**
     * ?¤ì • ?”ë©´??Stateless UI êµ¬í˜„ì²´ìž…?ˆë‹¤.
     * @param onEvent UI ?´ë²¤??ì²˜ë¦¬ë¥??„í•œ ì½œë°±?…ë‹ˆ??
     * @param onBack ?¤ë¡œê°€ê¸?ì½œë°±?…ë‹ˆ??
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
                            text = "?¤ì •",
                            style = typography.headingMedium18
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                painter = painterResource(Res.drawable.icon_24_arrow_left),
                                contentDescription = "?¤ë¡œê°€ê¸?,
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
                    title = "?Œë¦¼ ?¤ì •",
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
                    title = "ë¡œê·¸?„ì›ƒ",
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
                    title = "ê³„ì • ?ˆí‡´",
                    onClick = { onEvent(UiEvent.DeleteAccountClicked) }
                )
            }
        }
    }
}

/**
 * ?¤ì • ?”ë©´??ë©”ë‰´ ?„ì´?œìž…?ˆë‹¤.
 * @param icon ì¢Œì¸¡ ?„ì´ì½?Composable.
 * @param title ë©”ë‰´ ?œëª©.
 * @param onClick ë©”ë‰´ ?´ë¦­ ???¸ì¶œ?˜ëŠ” ì½œë°±?…ë‹ˆ??
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
 * ?¤ì • ?”ë©´ ?„ë¦¬ë·°ìž…?ˆë‹¤.
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
