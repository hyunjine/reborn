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
 * ?åÎ¶º ?§Ï†ï ?îÎ©¥.
 * ?åÎ¶º ?òÏã† ?¨Î?Î•??†Í??????àÎäî ?§ÏúÑÏπòÎ? ?úÍ≥µ?©Îãà??
 */
@Serializable
object NotificationSettingScreen : NavKey {

    /**
     * ?åÎ¶º ?§Ï†ï ?îÎ©¥?êÏÑú Î∞úÏÉù?òÎäî UI ?¥Î≤§?∏Îì§?ÖÎãà??
     */
    sealed interface UiEvent {
        /**
         * ?åÎ¶º ?†Í? Î≥ÄÍ≤???Î∞úÏÉù?òÎäî ?¥Î≤§?∏ÏûÖ?àÎã§.
         * @param enabled ?åÎ¶º ?úÏÑ±???¨Î?.
         */
        data class NotificationToggled(val enabled: Boolean) : UiEvent
    }

    /**
     * ?åÎ¶º ?§Ï†ï ?îÎ©¥??Stateful Wrapper?ÖÎãà??
     * @param viewModel Koin???µÌï¥ Ï£ºÏûÖ?òÎäî ViewModel?ÖÎãà??
     * @param onBack ?§Î°úÍ∞ÄÍ∏?ÏΩúÎ∞±?ÖÎãà??
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
     * ?åÎ¶º ?§Ï†ï ?îÎ©¥??Stateless UI Íµ¨ÌòÑÏ≤¥ÏûÖ?àÎã§.
     * @param isNotificationEnabled ?ÑÏû¨ ?åÎ¶º ?úÏÑ±???ÅÌÉú?ÖÎãà??
     * @param onEvent UI ?¥Î≤§??Ï≤òÎ¶¨Î•??ÑÌïú ÏΩúÎ∞±?ÖÎãà??
     * @param onBack ?§Î°úÍ∞ÄÍ∏?ÏΩúÎ∞±?ÖÎãà??
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
                            text = "?åÎ¶º ?§Ï†ï",
                            style = typography.headingMedium18
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                painter = painterResource(Res.drawable.icon_24_arrow_left),
                                contentDescription = "?§Î°úÍ∞ÄÍ∏?,
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
 * ?åÎ¶º ?§Ï†ï ?†Í? ?âÏûÖ?àÎã§.
 * Ï¢åÏ∏°??"?åÎ¶º ?§Ï†ï" ?çÏä§?? ?∞Ï∏°???§ÏúÑÏπòÎ? ?úÏãú?©Îãà??
 * @param isEnabled ?ÑÏû¨ ?åÎ¶º ?úÏÑ±???ÅÌÉú?ÖÎãà??
 * @param onToggle ?§ÏúÑÏπ?Î≥ÄÍ≤????∏Ï∂ú?òÎäî ÏΩúÎ∞±?ÖÎãà??
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
            text = "?åÎ¶º ?§Ï†ï",
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
 * ?åÎ¶º ?§Ï†ï ?îÎ©¥ ?ÑÎ¶¨Î∑?(?åÎ¶º ?úÏÑ±???ÅÌÉú).
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
 * ?åÎ¶º ?§Ï†ï ?îÎ©¥ ?ÑÎ¶¨Î∑?(?åÎ¶º ÎπÑÌôú?±Ìôî ?ÅÌÉú).
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
