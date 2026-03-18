package com.hyunjine.reborn.ui.my

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import coil3.compose.AsyncImage
import com.hyunjine.reborn.common.theme.RebornTheme
import com.hyunjine.reborn.common.theme.color
import com.hyunjine.reborn.common.theme.typography
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import reborn.composeapp.generated.resources.Res
import reborn.composeapp.generated.resources.icon_24_bell
import reborn.composeapp.generated.resources.icon_24_arrow_right
import reborn.composeapp.generated.resources.icon_24_location
import reborn.composeapp.generated.resources.icon_24_help
import reborn.composeapp.generated.resources.icon_24_setting
import reborn.composeapp.generated.resources.icon_24_store

/**
 * ???ïÎ≥¥ ?îÎ©¥.
 * ?ÖÏ≤¥ ?±Î°ù ?¨Î????∞Îùº ??Í∞ÄÏßÄ ?ÅÌÉú??UIÎ•??úÏãú?©Îãà??
 * - ÎØ∏Îì±Î°? ?ÖÏ≤¥ ?±Î°ù ?†ÎèÑ Î∞∞ÎÑà
 * - ?±Î°ù ?ÑÎ£å: ???ÖÏ≤¥ Ïπ¥Îìú
 */
@Serializable
object MyScreen : NavKey {

    /**
     * ???ïÎ≥¥ ?îÎ©¥??Stateful Wrapper?ÖÎãà??
     * @param viewModel Koin???µÌï¥ Ï£ºÏûÖ?òÎäî ViewModel?ÖÎãà??
     * @param onRegisterStore ?ÖÏ≤¥ ?±Î°ù ?îÎ©¥?ºÎ°ú ?¥Îèô?òÎäî ÏΩúÎ∞±?ÖÎãà??
     * @param onStoreDetail ???ÖÏ≤¥ ?ÅÏÑ∏ ?îÎ©¥?ºÎ°ú ?¥Îèô?òÎäî ÏΩúÎ∞±?ÖÎãà??
     * @param onSetting ?§Ï†ï ?îÎ©¥?ºÎ°ú ?¥Îèô?òÎäî ÏΩúÎ∞±?ÖÎãà??
     * @param onNotice Í≥µÏ??¨Ìï≠ ?îÎ©¥?ºÎ°ú ?¥Îèô?òÎäî ÏΩúÎ∞±?ÖÎãà??
     * @param onTerms ?úÎπÑ???¥Ïö©?ΩÍ? ?îÎ©¥?ºÎ°ú ?¥Îèô?òÎäî ÏΩúÎ∞±?ÖÎãà??
     * @param onCustomerService Í≥†Í∞ù?ºÌÑ∞ ?îÎ©¥?ºÎ°ú ?¥Îèô?òÎäî ÏΩúÎ∞±?ÖÎãà??
     */
    @Composable
    operator fun invoke(
        viewModel: MyViewModel = koinViewModel(),
        onRegisterStore: () -> Unit = {},
        onStoreDetail: () -> Unit = {},
        onSetting: () -> Unit = {},
        onNotice: () -> Unit = {},
        onTerms: () -> Unit = {},
        onCustomerService: () -> Unit = {}
    ) {
        val state by viewModel.state.collectAsStateWithLifecycle()
        invoke(
            state = state,
            onSettingClick = onSetting,
            onRegisterStoreClick = onRegisterStore,
            onStoreCardClick = onStoreDetail,
            onNoticeClick = onNotice,
            onTermsClick = onTerms,
            onCustomerServiceClick = onCustomerService
        )
    }

    /**
     * ???ïÎ≥¥ ?îÎ©¥??Stateless UI Íµ¨ÌòÑÏ≤¥ÏûÖ?àÎã§.
     * @param state ?ÑÏû¨ ?îÎ©¥??UI ?ÅÌÉú?ÖÎãà??
     * @param onSettingClick ?§Ï†ï ?ÑÏù¥ÏΩ??¥Î¶≠ ???∏Ï∂ú?òÎäî ÏΩúÎ∞±?ÖÎãà??
     * @param onRegisterStoreClick ?ÖÏ≤¥ ?±Î°ù?òÍ∏∞ Î≤ÑÌäº ?¥Î¶≠ ???∏Ï∂ú?òÎäî ÏΩúÎ∞±?ÖÎãà??
     * @param onStoreCardClick ???ÖÏ≤¥ Ïπ¥Îìú ?¥Î¶≠ ???∏Ï∂ú?òÎäî ÏΩúÎ∞±?ÖÎãà??
     * @param onNoticeClick Í≥µÏ??¨Ìï≠ Î©îÎâ¥ ?¥Î¶≠ ???∏Ï∂ú?òÎäî ÏΩúÎ∞±?ÖÎãà??
     * @param onTermsClick ?úÎπÑ???¥Ïö©?ΩÍ? Î©îÎâ¥ ?¥Î¶≠ ???∏Ï∂ú?òÎäî ÏΩúÎ∞±?ÖÎãà??
     * @param onCustomerServiceClick Í≥†Í∞ù?ºÌÑ∞ Î©îÎâ¥ ?¥Î¶≠ ???∏Ï∂ú?òÎäî ÏΩúÎ∞±?ÖÎãà??
     */
    @Composable
    operator fun invoke(
        state: MyModel,
        onSettingClick: () -> Unit = {},
        onRegisterStoreClick: () -> Unit = {},
        onStoreCardClick: () -> Unit = {},
        onNoticeClick: () -> Unit = {},
        onTermsClick: () -> Unit = {},
        onCustomerServiceClick: () -> Unit = {}
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            MyTopBar(
                onSettingClick = onSettingClick
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                ProfileCard(
                    userName = state.userName,
                    email = state.email
                )
                Spacer(modifier = Modifier.height(20.dp))
                if (state.hasStore && state.storeInfo != null) {
                    StoreCard(
                        storeInfo = state.storeInfo,
                        onClick = onStoreCardClick
                    )
                } else {
                    RegisterStoreBanner(
                        onRegisterClick = onRegisterStoreClick
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                MenuItem(
                    icon = { Icon(painterResource(Res.drawable.icon_24_bell), contentDescription = null, modifier = Modifier.size(20.dp), tint = color.gray900) },
                    title = "Í≥µÏ??¨Ìï≠",
                    onClick = onNoticeClick
                )
                MenuItem(
                    icon = { Icon(painterResource(Res.drawable.icon_24_bell), contentDescription = null, modifier = Modifier.size(20.dp), tint = color.gray900) },
                    title = "?úÎπÑ???¥Ïö©?ΩÍ?",
                    onClick = onTermsClick
                )
                MenuItem(
                    icon = { Icon(painterResource(Res.drawable.icon_24_help), contentDescription = null, modifier = Modifier.size(20.dp), tint = color.gray900) },
                    title = "Í≥†Í∞ù?ºÌÑ∞",
                    onClick = onCustomerServiceClick
                )
            }
        }
    }
}

/**
 * ???ïÎ≥¥ ?îÎ©¥???ÅÎã® Î∞îÏûÖ?àÎã§.
 * Ï¢åÏ∏°??"???ïÎ≥¥" ?Ä?¥Ì?, ?∞Ï∏°???§Ï†ï ?ÑÏù¥ÏΩòÏùÑ ?úÏãú?©Îãà??
 * @param onSettingClick ?§Ï†ï ?ÑÏù¥ÏΩ??¥Î¶≠ ???∏Ï∂ú?òÎäî ÏΩúÎ∞±?ÖÎãà??
 */
@Composable
private fun MyTopBar(
    onSettingClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(56.dp)
            .padding(start = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "???ïÎ≥¥",
            style = typography.headingBold24.copy(fontSize = 22.sp),
            color = color.gray900
        )
        IconButton(onClick = onSettingClick) {
            Icon(
                painter = painterResource(Res.drawable.icon_24_setting),
                contentDescription = "?§Ï†ï",
                modifier = Modifier.size(24.dp),
                tint = color.gray900
            )
        }
    }
}

/**
 * ?¨Ïö©???ÑÎ°ú??Ïπ¥Îìú?ÖÎãà??
 * ?¥Î¶ÑÍ≥??¥Î©î?ºÏùÑ ?úÏãú?©Îãà??
 * @param userName ?¨Ïö©???¥Î¶Ñ.
 * @param email ?¨Ïö©???¥Î©î??
 */
@Composable
private fun ProfileCard(
    userName: String,
    email: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, color.gray200, RoundedCornerShape(10.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            text = userName,
            style = typography.headingMedium20.copy(fontWeight = FontWeight.Bold),
            color = color.gray900
        )
        Text(
            text = email,
            style = typography.bodyRegular14,
            color = color.gray700
        )
    }
}

/**
 * ?ÖÏ≤¥ ÎØ∏Îì±Î°????úÏãú?òÎäî ?ÖÏ≤¥ ?±Î°ù ?†ÎèÑ Î∞∞ÎÑà?ÖÎãà??
 * @param onRegisterClick ?ÖÏ≤¥ ?±Î°ù?òÍ∏∞ Î≤ÑÌäº ?¥Î¶≠ ???∏Ï∂ú?òÎäî ÏΩúÎ∞±?ÖÎãà??
 */
@Composable
private fun RegisterStoreBanner(
    onRegisterClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, color.green300, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = color.green500.copy(alpha = 0.1f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.icon_24_store),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = color.green500
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Í≥†Î¨º?ÅÏùÑ ?¥ÏòÅ?òÏãú?òÏöî?",
                    style = typography.headingBold18,
                    color = color.gray900
                )
                Text(
                    text = "ÏßÄÍ∏?Î¶¨Î≥∏ ?åÌä∏?àÎ°ú ?±Î°ù?òÍ≥†\n??ÎßéÏ? Í≥†Í∞ù??ÎßåÎÇòÎ≥¥ÏÑ∏??,
                    style = typography.bodyRegular14,
                    color = color.gray800
                )
            }
        }
        Button(
            onClick = onRegisterClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = color.green500,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "?ÖÏ≤¥ ?±Î°ù?òÍ∏∞",
                style = typography.bodySemibold14,
                color = Color.White
            )
        }
    }
}

/**
 * ?ÖÏ≤¥ ?±Î°ù ?ÑÎ£å ???úÏãú?òÎäî ???ÖÏ≤¥ Ïπ¥Îìú?ÖÎãà??
 * ?ÖÏ≤¥ ?¥Î?ÏßÄ, ?¥Î¶Ñ, Ï£ºÏÜåÎ•??úÏãú?©Îãà??
 * @param storeInfo ?ÖÏ≤¥ ?ïÎ≥¥ Î™®Îç∏.
 * @param onClick Ïπ¥Îìú ?¥Î¶≠ ???∏Ï∂ú?òÎäî ÏΩúÎ∞±?ÖÎãà??
 */
@Composable
private fun StoreCard(
    storeInfo: MyStoreModel,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(color.gray50)
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = storeInfo.imageUrl,
            contentDescription = "${storeInfo.name} ?Ä???¥Î?ÏßÄ",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(101.dp)
                .background(color.gray200)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = storeInfo.name,
                    style = typography.headingBold18,
                    color = color.gray900,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.icon_24_location),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = color.gray700
                    )
                    Text(
                        text = storeInfo.address,
                        style = typography.bodyRegular14,
                        color = color.gray700,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                painter = painterResource(Res.drawable.icon_24_arrow_right),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = color.gray600
            )
        }
    }
}

/**
 * Î©îÎâ¥ ?ÑÏù¥?úÏûÖ?àÎã§.
 * Ï¢åÏ∏°???ÑÏù¥ÏΩ? Ï§ëÏïô???úÎ™©, ?∞Ï∏°???îÏÇ¥?úÎ? ?úÏãú?©Îãà??
 * @param icon ?ÑÏù¥ÏΩ?Composable.
 * @param title Î©îÎâ¥ ?úÎ™©.
 * @param onClick Î©îÎâ¥ ?¥Î¶≠ ???∏Ï∂ú?òÎäî ÏΩúÎ∞±?ÖÎãà??
 */
@Composable
private fun MenuItem(
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
 * ???ïÎ≥¥ ?îÎ©¥ ?ÑÎ¶¨Î∑?(?ÖÏ≤¥ ÎØ∏Îì±Î°??ÅÌÉú).
 */
@Preview(showBackground = true)
@Composable
private fun MyScreenNoStorePreview() {
    RebornTheme {
        MyScreen(
            state = MyModel(
                userName = "ÍπÄÏ≤†Ïàò",
                email = "kimcs@example.com",
                hasStore = false,
                storeInfo = null
            )
        )
    }
}

/**
 * ???ïÎ≥¥ ?îÎ©¥ ?ÑÎ¶¨Î∑?(?ÖÏ≤¥ ?±Î°ù ?ÑÎ£å ?ÅÌÉú).
 */
@Preview(showBackground = true)
@Composable
private fun MyScreenWithStorePreview() {
    RebornTheme {
        MyScreen(
            state = MyModel(
                userName = "ÍπÄÏ≤†Ïàò",
                email = "kimcs@example.com",
                hasStore = true,
                storeInfo = MyStoreModel(
                    name = "?úÏö∏Í≥†Î¨º??,
                    address = "?úÏö∏?πÎ≥Ñ??Í∞ïÎÇ®Íµ???Çº??123-45",
                    imageUrl = ""
                )
            )
        )
    }
}
