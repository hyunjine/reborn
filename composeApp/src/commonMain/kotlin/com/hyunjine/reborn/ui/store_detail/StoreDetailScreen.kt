package com.hyunjine.reborn.ui.store_detail

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import coil3.compose.AsyncImage
import com.hyunjine.reborn.common.theme.RebornTheme
import com.hyunjine.reborn.common.theme.color
import com.hyunjine.reborn.common.theme.typography
import com.hyunjine.reborn.common.util.fullName
import com.hyunjine.reborn.common.util.ClipboardManager
import com.hyunjine.reborn.common.util.clickable
import com.hyunjine.reborn.common.util.pad
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.number
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import reborn.composeapp.generated.resources.Res
import reborn.composeapp.generated.resources.icon_24_arrow_left
import reborn.composeapp.generated.resources.icon_24_copy
import reborn.composeapp.generated.resources.icon_24_location_pin

/**
 * ?ÖÏ≤¥ ?ÅÏÑ∏ ?òÏù¥ÏßÄ ?îÎ©¥.
 * ?ÖÏ≤¥??Í∏∞Î≥∏ ?ïÎ≥¥, ?ÅÏóÖ ?úÍ∞Ñ, ?§ÏãúÍ∞?Îß§ÏûÖ ?úÏÑ∏Î•??úÏãú?©Îãà??
 * @param storeId ?ÖÏ≤¥ ID
 */
@Serializable
data class StoreDetailScreen(
    val storeId: Long
) : NavKey {

    /**
     * ?ÖÏ≤¥ ?ÅÏÑ∏ ?òÏù¥ÏßÄ??UI ?¥Î≤§??
     */
    sealed interface UiEvent {
        /** ?§Î°úÍ∞ÄÍ∏?Î≤ÑÌäº ?¥Î¶≠ */
        data object BackClicked : UiEvent

        /** Ï£ºÏÜå Î≥µÏÇ¨ Î≤ÑÌäº ?¥Î¶≠ */
        data class CopyAddressClicked(val value: String) : UiEvent

        /** ?ÑÌôî Î¨∏Ïùò?òÍ∏∞ Î≤ÑÌäº ?¥Î¶≠ */
        data object CallClicked : UiEvent
    }

    /**
     * Stateful Wrapper. Koin ViewModel??Ï£ºÏûÖÎ∞õÍ≥† ?¥Î≤§?∏Î? Ï≤òÎ¶¨?©Îãà??
     * @param viewModel Koin?êÏÑú Ï£ºÏûÖÎ∞õÎäî ViewModel
     * @param onBack ?§Î°úÍ∞ÄÍ∏?ÏΩúÎ∞±
     */
    @Composable
    operator fun invoke(
        viewModel: StoreDetailViewModel = koinViewModel { parametersOf(storeId) },
        onBack: () -> Unit = {}
    ) {
        val model by viewModel.model.collectAsStateWithLifecycle()
        val uriHandler = LocalUriHandler.current
        invoke(
            model = model,
            onEvent = { event ->
                when (event) {
                    is UiEvent.BackClicked -> onBack()
                    is UiEvent.CopyAddressClicked -> {
                        ClipboardManager().copyToClipboard(event.value)
                    }
                    is UiEvent.CallClicked -> {
                        uriHandler.openUri("tel:${model.phoneNumber}")
                    }
                    else -> viewModel.event(event)
                }
            }
        )
    }

    /**
     * Stateless UI. ?úÏàò ComposableÎ°?UIÎ•?Í∑∏Î¶Ω?àÎã§.
     * @param model ?ÑÏû¨ UI ?ÅÌÉú
     * @param onEvent UI ?¥Î≤§??ÏΩúÎ∞±
     */
    @Composable
    operator fun invoke(
        model: StoreDetailModel,
        onEvent: (UiEvent) -> Unit = {}
    ) {
        Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 100.dp)
            ) {
                StoreImageSection(
                    imageUrls = model.imageUrls,
                    onBackClick = { onEvent(UiEvent.BackClicked) }
                )
                StoreInfoSection(
                    name = model.name,
                    address = model.address,
                    onCopyClick = { onEvent(UiEvent.CopyAddressClicked(it)) }
                )
                StoreDescriptionSection(
                    description = model.description,
                    businessHours = model.businessHours
                )
                StorePriceSection(
                    prices = model.prices,
                    lastUpdated = model.lastUpdated
                )
            }
            CallButton(
                modifier = Modifier.align(Alignment.BottomCenter),
                onClick = { onEvent(UiEvent.CallClicked) }
            )
        }
    }
}

/**
 * ?ÖÏ≤¥ ?¥Î?ÏßÄ ?πÏÖò. ?¥Î?ÏßÄ Î∑∞Ìéò?¥Ï??Ä ?§Î°úÍ∞ÄÍ∏?Î≤ÑÌäº, ?òÏù¥ÏßÄ ?∏ÎîîÏºÄ?¥ÌÑ∞Î•??úÏãú?©Îãà??
 * @param imageUrls ?ÖÏ≤¥ ?¥Î?ÏßÄ URL Î™©Î°ù
 * @param onBackClick ?§Î°úÍ∞ÄÍ∏?Î≤ÑÌäº ?¥Î¶≠ ÏΩúÎ∞±
 */
@Composable
private fun StoreImageSection(
    imageUrls: ImmutableList<String>,
    onBackClick: () -> Unit
) {
    val pageCount = imageUrls.size.coerceAtLeast(1)
    val pagerState = rememberPagerState { pageCount }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(378f / 236f)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            if (imageUrls.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color.gray100)
                )
            } else {
                AsyncImage(
                    model = imageUrls[page],
                    contentDescription = "?ÖÏ≤¥ ?¥Î?ÏßÄ ${page + 1}",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        // Back button
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .statusBarsPadding()
                .padding(start = 8.dp, top = 8.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(
                painter = painterResource(Res.drawable.icon_24_arrow_left),
                contentDescription = "?§Î°úÍ∞ÄÍ∏?,
                modifier = Modifier.size(24.dp),
                tint = color.gray900
            )
        }

        // Page indicators
        if (pageCount > 1) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                repeat(pageCount) { index ->
                    val isSelected = pagerState.currentPage == index
                    val width by animateDpAsState(
                        targetValue = if (isSelected) 24.dp else 6.dp,
                        animationSpec = tween(durationMillis = 300)
                    )
                    Box(
                        modifier = Modifier
                            .width(width)
                            .height(6.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) Color.White
                                else Color.White.copy(alpha = 0.6f)
                            )
                    )
                }
            }
        }
    }
}

/**
 * ?ÖÏ≤¥ Í∏∞Î≥∏ ?ïÎ≥¥ ?πÏÖò. ?ÖÏ≤¥Î™? ?∏Ï¶ù Î∞∞Ï?, Ï£ºÏÜåÎ•??úÏãú?©Îãà??
 * @param name ?ÖÏ≤¥Î™?
 * @param address ?ÖÏ≤¥ Ï£ºÏÜå
 * @param onCopyClick Ï£ºÏÜå Î≥µÏÇ¨ Î≤ÑÌäº ?¥Î¶≠ ÏΩúÎ∞±
 */
@Composable
private fun StoreInfoSection(
    name: String,
    address: String,
    onCopyClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        // Store name with verified badge
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = name,
                style = typography.headingBold24,
                color = color.gray900
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Address with inline copy icon
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(Res.drawable.icon_24_location_pin),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = color.gray400
            )
            Spacer(modifier = Modifier.width(8.dp))

            val copyIconId = "copy_icon"
            val annotatedAddress = buildAnnotatedString {
                append("$address ")
                appendInlineContent(copyIconId)
            }
            val inlineContent = mapOf(
                copyIconId to InlineTextContent(
                    Placeholder(
                        width = 16.sp,
                        height = 16.sp,
                        placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                    )
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.icon_24_copy),
                        contentDescription = "Ï£ºÏÜå Î≥µÏÇ¨",
                        modifier = Modifier.size(16.dp),
                        tint = color.gray400
                    )
                }
            )
            Text(
                text = annotatedAddress,
                inlineContent = inlineContent,
                style = typography.bodyRegular16,
                color = color.gray800,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onCopyClick(address) }
            )
        }
    }
}

/**
 * ?ÖÏ≤¥ ?åÍ∞ú Î∞??ÅÏóÖ ?úÍ∞Ñ ?πÏÖò.
 * @param description ?ÖÏ≤¥ ?åÍ∞ú ?çÏä§??
 * @param businessHours ?ÅÏóÖ ?úÍ∞Ñ Î™©Î°ù
 */
@Composable
private fun StoreDescriptionSection(
    description: String,
    businessHours: ImmutableList<OperationTimeModel>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        // Section title
        Text(
            text = "?ÖÏ≤¥ ?åÍ∞ú",
            style = typography.headingSemibold18,
            color = color.gray900
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Description
        Text(
            text = description,
            style = typography.bodyRegular16.copy(lineHeight = 26.sp),
            color = color.gray800
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Business hours title
        Text(
            text = "?ÅÏóÖ ?úÍ∞Ñ",
            style = typography.titleSemibold16,
            color = color.gray900
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Business hours list
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            businessHours.forEach { hour ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = hour.dayOfWeek.fullName,
                        style = typography.bodyRegular16,
                        color = color.gray700
                    )
                    Text(
                        text = hour.operation.toString(),
                        style = typography.bodyMedium16,
                        color = if (hour.operation == Operation.Closed) color.gray400 else color.gray900
                    )
                }
            }
        }
    }
}

/**
 * ?§ÏãúÍ∞?Îß§ÏûÖ ?úÏÑ∏ ?πÏÖò.
 * @param prices Îß§ÏûÖ ?úÏÑ∏ Î™©Î°ù
 * @param lastUpdated ÏµúÏ¢Ö ?ÖÎç∞?¥Ìä∏ ?úÍ∞Ñ ?çÏä§??
 */
@Composable
private fun StorePriceSection(
    prices: ImmutableList<StorePriceModel>,
    lastUpdated: LocalDateTime
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        val lastUpdated = lastUpdated.run {
            "${year}.${month.number.pad()}.${day.pad()} ${hour.pad()}:${minute.pad()}"
        }
        Text(
            text = "?§ÏãúÍ∞?Îß§ÏûÖ ?úÏÑ∏",
            style = typography.headingSemibold18,
            color = color.gray900
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "ÏµúÍ∑º ?ÖÎç∞?¥Ìä∏: $lastUpdated",
            style = typography.captionRegular14,
            color = color.gray600
        )
        Spacer(modifier = Modifier.height(6.dp))

        // Price list
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            prices.forEach { price ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color.gray50, RoundedCornerShape(10.dp))
                        .padding(horizontal = 16.dp, vertical = 18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = price.name,
                        style = typography.bodyMedium16,
                        color = color.gray900
                    )
                    Text(
                        text = price.price,
                        style = typography.headingBold18,
                        color = color.gray900
                    )
                }
            }
        }
    }
}

/**
 * ?òÎã® ?ÑÌôî Î¨∏Ïùò?òÍ∏∞ Î≤ÑÌäº.
 * @param modifier Modifier
 * @param onClick Î≤ÑÌäº ?¥Î¶≠ ÏΩúÎ∞±
 */
@Composable
private fun CallButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 17.dp)
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = color.green500
            )
        ) {
            Text(
                text = "?ÑÌôî Î¨∏Ïùò?òÍ∏∞",
                style = typography.titleSemibold16,
                color = Color.White
            )
        }
    }
}

/**
 * ?§ÏãúÍ∞?Îß§ÏûÖ ?úÏÑ∏ ?πÏÖò ÎØ∏Î¶¨Î≥¥Í∏∞.
 */
@Preview(showBackground = true)
@Composable
private fun StorePriceSectionPreview() {
    RebornTheme {
        StorePriceSection(
            prices = persistentListOf(
                StorePriceModel("Í≥†Ï≤†", "450??kg"),
                StorePriceModel("?åÎ£®ÎØ∏ÎäÑ", "1,800??kg"),
                StorePriceModel("Íµ¨Î¶¨", "8,500??kg"),
                StorePriceModel("?§ÌÖê", "1,150??kg")
            ),
            lastUpdated = LocalDateTime(2026, 3, 12, 14, 30)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StoreDetailScreenPreview() {
    RebornTheme {
        StoreDetailScreen(storeId = 1).invoke(
            model = StoreDetailModel(
                name = "?úÏö∏Í≥†Î¨º??,
                address = "?úÏö∏?πÎ≥Ñ??Í∞ïÎÇ®Íµ???Çº??123-45",
                description = "?ïÌôï??Í≥ÑÍ∑º ?ΩÏÜç, ?Ä??Îß§ÏûÖ ??Ï∂îÍ? ?®Í? ?ëÏùò Í∞Ä?•Ìï©?àÎã§. 30???ÑÌÜµ???†Î¢∞?????àÎäî Í≥†Î¨º?ÅÏûÖ?àÎã§.",
                businessHours = DayOfWeek.entries.map {
                    OperationTimeModel(
                        dayOfWeek = it,
                        operation = if (it == DayOfWeek.THURSDAY) {
                            Operation.Closed
                        } else {
                            Operation.Open(
                                start = LocalTime(hour = 5, minute = 0), end = LocalTime(hour = 21, minute = 30)
                            )
                        }
                    )
                }.toImmutableList(),
                prices = persistentListOf(
                    StorePriceModel("Í≥†Ï≤†", "450??kg"),
                    StorePriceModel("?åÎ£®ÎØ∏ÎäÑ", "1,800??kg"),
                    StorePriceModel("Íµ¨Î¶¨", "8,500??kg"),
                    StorePriceModel("?§ÌÖê", "1,150??kg")
                ),
                lastUpdated = LocalDateTime(2026, 3, 12, 14, 30),
                phoneNumber = "010-1234-5678",
                id = 1
            )
        )
    }
}
