package com.hyunjine.reborn.ui.regist_store

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import com.hyunjine.reborn.common.component.AddressSearchDialog
import com.hyunjine.reborn.common.component.ImagePickerLauncher
import com.hyunjine.reborn.common.component.ItemPickerBottomSheet
import com.hyunjine.reborn.common.component.TimePickerBottomSheet
import com.hyunjine.reborn.common.theme.RebornTheme
import com.hyunjine.reborn.common.theme.color
import com.hyunjine.reborn.common.theme.typography
import com.hyunjine.reborn.common.util.decodeToImageBitmap
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import reborn.composeapp.generated.resources.Res
import reborn.composeapp.generated.resources.icon_24_add
import reborn.composeapp.generated.resources.icon_24_arrow_left
import reborn.composeapp.generated.resources.icon_24_camera
import reborn.composeapp.generated.resources.icon_24_close
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import com.hyunjine.reborn.common.util.shortName
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toPersistentHashMap
import kotlinx.coroutines.channels.consumeEach
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime

/**
 * ?…ì²´ ?±ë¡ ?”ë©´.
 * ?¬ì§„, ê¸°ë³¸ ?•ë³´, ?ì—… ?œê°„, ë§¤ì… ?¨ê?ë¥??…ë ¥?˜ì—¬ ?…ì²´ë¥??±ë¡?©ë‹ˆ??
 */
@Serializable
object RegistStoreScreen : NavKey {

    /**
     * ?…ì²´ ?±ë¡ ?”ë©´??UI ?´ë²¤??
     */
    sealed interface UiEvent {

        /** ?¬ì§„ ì¶”ê? */
        data class PhotosAdded(val photos: List<ByteArray>) : UiEvent

        /** ?¬ì§„ ?? œ */
        data class PhotoRemoved(val index: Int) : UiEvent

        /** ?…ì²´ëª?ë³€ê²?*/
        data class StoreNameChanged(val name: String) : UiEvent

        /** ?„í™”ë²ˆí˜¸ ë³€ê²?*/
        data class PhoneChanged(val phone: String) : UiEvent

        /** ì£¼ì†Œ ë³€ê²?*/
        data class AddressChanged(val address: String) : UiEvent

        /** ?…ì²´ ?Œê°œ ë³€ê²?*/
        data class DescriptionChanged(val description: String) : UiEvent

        /** ?¼ê´„ ?œì‘ ?œê°„ ë³€ê²?*/
        data class BatchStartTimeChanged(val time: LocalTime) : UiEvent

        /** ?¼ê´„ ì¢…ë£Œ ?œê°„ ë³€ê²?*/
        data class BatchEndTimeChanged(val time: LocalTime) : UiEvent

        /** ?¼ê´„ ?œê°„ ?ìš© */
        data object ApplyBatchTime : UiEvent

        /** ?”ì¼ ?œì„±???íƒœ ë³€ê²?*/
        data class DayEnabledChanged(val key: DayOfWeek, val enabled: Boolean) : UiEvent

        /** ?”ì¼ë³??œì‘ ?œê°„ ë³€ê²?*/
        data class DayStartTimeChanged(val key: DayOfWeek, val time: LocalTime) : UiEvent

        /** ?”ì¼ë³?ì¢…ë£Œ ?œê°„ ë³€ê²?*/
        data class DayEndTimeChanged(val key: DayOfWeek, val time: LocalTime) : UiEvent

        /** ?ˆëª© ì¶”ê? */
        data object AddPriceItem : UiEvent

        /** ?ˆëª© ?? œ */
        data class RemovePriceItem(val index: Int) : UiEvent

        /** ?ˆëª©ëª?ë³€ê²?*/
        data class PriceItemNameChanged(val index: Int, val name: ItemName) : UiEvent

        /** ?ˆëª© ?¨ê? ë³€ê²?*/
        data class PriceItemPriceChanged(val index: Int, val price: Int?) : UiEvent

        /** ì£¼ì†Œ ê²€???¤ì´?¼ë¡œê·??œì‹œ ?íƒœ ë³€ê²?*/
        data class AddressSearchState(val isShow: Boolean) : UiEvent

        /** ?±ë¡?˜ê¸° ?´ë¦­ */
        data object SubmitClicked : UiEvent
    }

    /**
     * Stateful Wrapper. Koin ViewModel??ì£¼ì…ë°›ê³  ?´ë²¤?¸ë? ì²˜ë¦¬?©ë‹ˆ??
     * @param viewModel Koin?ì„œ ì£¼ì…ë°›ëŠ” ViewModel
     * @param onBack ?¤ë¡œê°€ê¸?ì½œë°±
     */

    @Composable
    operator fun invoke(
        viewModel: RegistStoreViewModel = koinViewModel(),
        onBack: () -> Unit = {}
    ) {
        val model by viewModel.model.collectAsStateWithLifecycle()
        val addressState by viewModel.addressWindowState.collectAsStateWithLifecycle()
        val snackbarHostState = remember { SnackbarHostState() }


        LaunchedEffect(Unit) {
            viewModel.effects.consumeEach { effect ->
                when (effect) {
                    is RegistStoreViewModel.Effect.ShowSnackbar -> {
                        snackbarHostState.showSnackbar(effect.message)
                    }
                }
            }
        }

        invoke(
            model = model,
            addressState = addressState,
            snackbarHostState = snackbarHostState,
            onBack = onBack,
            onEvent = viewModel::event
        )
    }

    /**
     * Stateless UI. ?œìˆ˜ Composableë¡?UIë¥?ê·¸ë¦½?ˆë‹¤.
     * @param model ?„ì¬ UI ?íƒœ
     * @param snackbarHostState ?¤ë‚µë°??¸ìŠ¤???íƒœ
     * @param onEvent UI ?´ë²¤??ì½œë°±
     */
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    operator fun invoke(
        model: RegistStoreModel,
        addressState: Boolean,
        snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
        onBack: () -> Unit = {},
        onEvent: (UiEvent) -> Unit = {}
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "?…ì²´ ?±ë¡",
                            style = typography.headingMedium18,
                            color = color.gray900
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                painter = painterResource(Res.drawable.icon_24_arrow_left),
                                contentDescription = "?¤ë¡œê°€ê¸?,
                                modifier = Modifier.size(24.dp),
                                tint = color.gray900
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White
                    )
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            containerColor = Color.White
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                PhotoSection(
                    photos = model.photos,
                    onPhotosAdded = { onEvent(UiEvent.PhotosAdded(it)) },
                    onPhotoRemoved = { onEvent(UiEvent.PhotoRemoved(it)) }
                )
                SectionDivider()
                BasicInfoSection(
                    storeName = model.name,
                    phone = model.phone,
                    address = model.address,
                    description = model.description,
                    isShowingAddressSearch = addressState,
                    onStoreNameChanged = { onEvent(UiEvent.StoreNameChanged(it)) },
                    onPhoneChanged = { onEvent(UiEvent.PhoneChanged(it)) },
                    onAddressChanged = { onEvent(UiEvent.AddressChanged(it)) },
                    onDescriptionChanged = { onEvent(UiEvent.DescriptionChanged(it)) },
                    requestAddressSearchState = { onEvent(UiEvent.AddressSearchState(it)) }
                )
                SectionDivider()
                BusinessHoursSection(
                    batchStartTime = model.batchStartTime,
                    batchEndTime = model.batchEndTime,
                    daySchedules = model.daySchedules,
                    onBatchStartTimeChanged = { onEvent(UiEvent.BatchStartTimeChanged(it)) },
                    onBatchEndTimeChanged = { onEvent(UiEvent.BatchEndTimeChanged(it)) },
                    onApplyBatchTime = { onEvent(UiEvent.ApplyBatchTime) },
                    onDayEnabledChanged = { i, e -> onEvent(UiEvent.DayEnabledChanged(i, e)) },
                    onDayStartTimeChanged = { i, t -> onEvent(UiEvent.DayStartTimeChanged(i, t)) },
                    onDayEndTimeChanged = { i, t -> onEvent(UiEvent.DayEndTimeChanged(i, t)) }
                )
                SectionDivider()
                PriceSection(
                    priceItems = model.priceItems,
                    onAddPriceItem = { onEvent(UiEvent.AddPriceItem) },
                    onRemoveItem = { onEvent(UiEvent.RemovePriceItem(it)) },
                    onNameChanged = { i, n -> onEvent(UiEvent.PriceItemNameChanged(i, n)) },
                    onPriceChanged = { i, p -> onEvent(UiEvent.PriceItemPriceChanged(i, p)) }
                )
                InfoNotice()
                SubmitButton(onClick = { onEvent(UiEvent.SubmitClicked) })
            }
        }
    }
}

/**
 * ?¬ì§„ ?±ë¡ ?¹ì…˜.
 * ê°¤ëŸ¬ë¦¬ì—???´ë?ì§€ë¥?? íƒ?˜ê³  ? íƒ???´ë?ì§€ë¥??¸ë„¤?¼ë¡œ ?œì‹œ?©ë‹ˆ??
 * @param photos ?„ì¬ ?±ë¡???¬ì§„ ByteArray ëª©ë¡
 * @param maxPhotoCount ìµœë? ?±ë¡ ê°€???¬ì§„ ??
 * @param onPhotosAdded ?¬ì§„ ì¶”ê? ì½œë°±
 * @param onPhotoRemoved ?¬ì§„ ?? œ ì½œë°±
 */
@Composable
private fun PhotoSection(
    photos: ImmutableList<ByteArray>,
    onPhotosAdded: (List<ByteArray>) -> Unit,
    onPhotoRemoved: (Int) -> Unit
) {
    val maxPhotoCount = 5
    val remaining = maxPhotoCount - photos.size

    Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
        Text(
            text = "?¬ì§„ ?±ë¡",
            style = typography.headingMedium20,
            color = color.gray900
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "ìµœë? ${maxPhotoCount}?¥ê¹Œì§€ ?±ë¡ ê°€?¥í•©?ˆë‹¤",
            style = typography.bodyRegular14,
            color = color.gray600
        )
        Spacer(modifier = Modifier.height(16.dp))

        ImagePickerLauncher(
            maxSelection = remaining.coerceAtLeast(1),
            onResult = { selected ->
                if (selected.isNotEmpty()) onPhotosAdded(selected)
            }
        ) { launch ->
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // ? íƒ???¬ì§„ ?¸ë„¤??
                itemsIndexed(photos) { index, photoBytes ->
                    PhotoThumbnail(
                        photoBytes = photoBytes,
                        onRemove = { onPhotoRemoved(index) }
                    )
                }

                // ì¶”ê? ë²„íŠ¼ (?¬ì§„??ìµœë? ??ë¯¸ë§Œ???Œë§Œ ?œì‹œ)
                if (photos.size < maxPhotoCount) {
                    item {
                        OutlinedButton(
                            onClick = launch,
                            modifier = Modifier.size(96.dp),
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(2.dp, color.gray300),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = color.gray50
                            )
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.icon_24_camera),
                                    contentDescription = "?¬ì§„ ì¶”ê?",
                                    modifier = Modifier.size(24.dp),
                                    tint = color.gray400
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "${photos.size}/$maxPhotoCount",
                                    style = typography.captionMedium12,
                                    color = color.gray600
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * ?¬ì§„ ?¸ë„¤?? ? íƒ???´ë?ì§€ë¥??œì‹œ?˜ê³  ?? œ ë²„íŠ¼???œê³µ?©ë‹ˆ??
 * @param photoBytes ?´ë?ì§€ ByteArray
 * @param onRemove ?? œ ì½œë°±
 */
@Composable
private fun PhotoThumbnail(
    photoBytes: ByteArray,
    onRemove: () -> Unit
) {
    Box(modifier = Modifier.size(96.dp)) {
        Image(
            bitmap = remember(photoBytes) {
                photoBytes.decodeToImageBitmap()
            },
            contentDescription = "?±ë¡???¬ì§„",
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(14.dp)),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .size(20.dp)
                .align(Alignment.TopEnd)
                .padding(top = 4.dp, end = 4.dp)
                .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                .clickable { onRemove() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.icon_24_close),
                contentDescription = "?¬ì§„ ?? œ",
                modifier = Modifier.size(10.dp),
                tint = Color.White
            )
        }
    }
}

/**
 * ?¹ì…˜ êµ¬ë¶„??
 */
@Composable
private fun SectionDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
            .background(color.gray50)
    )
}

/**
 * ê¸°ë³¸ ?•ë³´ ?¹ì…˜.
 * @param storeName ?…ì²´ëª?
 * @param phone ?„í™”ë²ˆí˜¸
 * @param address ì£¼ì†Œ
 * @param description ?…ì²´ ?Œê°œ
 * @param isShowingAddressSearch ì£¼ì†Œ ê²€???¤ì´?¼ë¡œê·??œì‹œ ?¬ë?
 * @param onStoreNameChanged ?…ì²´ëª?ë³€ê²?ì½œë°±
 * @param onPhoneChanged ?„í™”ë²ˆí˜¸ ë³€ê²?ì½œë°±
 * @param onAddressChanged ì£¼ì†Œ ë³€ê²?ì½œë°±
 * @param onDescriptionChanged ?…ì²´ ?Œê°œ ë³€ê²?ì½œë°±
 * @param requestAddressSearchState ì£¼ì†Œ ê²€???¤ì´?¼ë¡œê·??œì‹œ ?íƒœ ë³€ê²??”ì²­ ì½œë°±
 */
@Composable
private fun BasicInfoSection(
    storeName: String,
    phone: String,
    address: String,
    description: String,
    isShowingAddressSearch: Boolean,
    onStoreNameChanged: (String) -> Unit,
    onPhoneChanged: (String) -> Unit,
    onAddressChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    requestAddressSearchState: (Boolean) -> Unit
) {
    val phoneFocusRequester = remember { FocusRequester() }
    val descriptionFocusRequester = remember { FocusRequester() }

    Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
        Text(
            text = "ê¸°ë³¸ ?•ë³´",
            style = typography.headingMedium20,
            color = color.gray900
        )
        Spacer(modifier = Modifier.height(16.dp))

        // ?…ì²´ëª?
        RequiredLabel("?…ì²´ëª?)
        Spacer(modifier = Modifier.height(8.dp))
        FormTextField(
            value = storeName,
            onValueChange = onStoreNameChanged,
            placeholder = "?…ì²´ëª…ì„ ?…ë ¥?´ì£¼?¸ìš”",
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { phoneFocusRequester.requestFocus() }
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        // ?„í™”ë²ˆí˜¸
        RequiredLabel("?„í™”ë²ˆí˜¸")
        Spacer(modifier = Modifier.height(8.dp))
        var phoneTextFieldValue by remember(phone) {
            mutableStateOf(TextFieldValue(text = phone, selection = TextRange(phone.length)))
        }
        BasicTextField(
            value = phoneTextFieldValue,
            onValueChange = { newValue ->
                val digits = newValue.text.filter { it.isDigit() }.take(11)
                onPhoneChanged(digits)
                phoneTextFieldValue = newValue.copy(text = digits)
            },
            singleLine = true,
            textStyle = typography.bodyRegular16.copy(color = color.gray900),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { requestAddressSearchState(true) }
            ),
            modifier = Modifier
                .focusRequester(phoneFocusRequester)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        phoneTextFieldValue = phoneTextFieldValue.copy(
                            selection = TextRange(phoneTextFieldValue.text.length)
                        )
                    }
                },
            visualTransformation = PhoneNumberTransformation,
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(color.gray50, RoundedCornerShape(8.dp))
                        .border(1.dp, color.gray200, RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (phone.isEmpty()) {
                        Text(
                            text = "?„í™”ë²ˆí˜¸ë¥??…ë ¥?´ì£¼?¸ìš”",
                            style = typography.bodyRegular16,
                            color = color.gray500
                        )
                    }
                    innerTextField()
                }
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // ì£¼ì†Œ
        RequiredLabel("ì£¼ì†Œ")
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(color.gray50)
                .border(1.dp, color.gray200, RoundedCornerShape(8.dp))
                .clickable { requestAddressSearchState(true) }
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = address.ifEmpty { "ì£¼ì†Œë¥?ê²€?‰í•´ì£¼ì„¸?? },
                style = typography.bodyRegular16,
                color = if (address.isEmpty()) color.gray500 else color.gray900,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        if (isShowingAddressSearch) {
            AddressSearchDialog(
                onAddressSelected = { selectedAddress ->
                    onAddressChanged(selectedAddress)
                    requestAddressSearchState(false)
                },
                onDismiss = { requestAddressSearchState(false) }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ?…ì²´ ?Œê°œ
        Text(
            text = "?…ì²´ ?Œê°œ",
            style = typography.bodyMedium14,
            color = color.gray800
        )
        Spacer(modifier = Modifier.height(8.dp))
        FormTextField(
            value = description,
            onValueChange = onDescriptionChanged,
            placeholder = "?…ì²´ë¥??Œê°œ?´ì£¼?¸ìš”\n?? 20??ê²½ë ¥??? ë¢°?????ˆëŠ” ê³ ë¬¼?ì…?ˆë‹¤.",
            minHeight = 120,
            singleLine = false,
            modifier = Modifier.focusRequester(descriptionFocusRequester),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default)
        )
    }
}

/**
 * ?„ìˆ˜ ?…ë ¥ ?¼ë²¨ (?´ë¦„ + ë¹¨ê°„ ë³„í‘œ).
 * @param text ?¼ë²¨ ?ìŠ¤??
 */
@Composable
private fun RequiredLabel(text: String) {
    Text(
        text = buildAnnotatedString {
            append("$text ")
            withStyle(SpanStyle(color = color.red500)) {
                append("*")
            }
        },
        style = typography.bodyMedium14,
        color = color.gray800
    )
}

/**
 * ê³µí†µ ?…ë ¥ ?„ë“œ.
 * @param value ?„ì¬ ê°?
 * @param onValueChange ê°?ë³€ê²?ì½œë°±
 * @param placeholder ?Œë ˆ?´ìŠ¤?€???ìŠ¤??
 * @param minHeight ìµœì†Œ ?’ì´ (dp)
 * @param singleLine ??ì¤??…ë ¥ ?¬ë?
 * @param modifier Modifier
 * @param keyboardOptions ?¤ë³´???µì…˜
 * @param keyboardActions ?¤ë³´???¡ì…˜
 */
@Composable
private fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    minHeight: Int = 48,
    singleLine: Boolean = true,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = singleLine,
        textStyle = typography.bodyRegular16.copy(color = color.gray900),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        modifier = modifier,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(minHeight.dp)
                    .background(color.gray50, RoundedCornerShape(8.dp))
                    .border(1.dp, color.gray200, RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = if (singleLine) 0.dp else 8.dp),
                contentAlignment = if (singleLine) Alignment.CenterStart else Alignment.TopStart
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = typography.bodyRegular16,
                        color = color.gray500
                    )
                }
                innerTextField()
            }
        }
    )
}

/**
 * ?œê°„ ? íƒ ?„ë“œ. ?´ë¦­ ??TimePickerBottomSheetë¥??œì‹œ?©ë‹ˆ??
 * @param value ?„ì¬ ?œê°„ ê°?
 * @param onValueChange ?œê°„ ë³€ê²?ì½œë°±
 * @param modifier Modifier
 * @param backgroundColor ë°°ê²½??
 */
@Composable
private fun TimePickerField(
    value: LocalTime,
    onValueChange: (LocalTime) -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = color.gray50
) {
    var showBottomSheet by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .height(39.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor)
            .border(1.dp, color.gray200, RoundedCornerShape(10.dp))
            .clickable { showBottomSheet = true }
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = formatTime(value.hour, value.minute),
            style = typography.bodyMedium14,
            color = color.gray900,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }

    if (showBottomSheet) {
        TimePickerBottomSheet(
            initialHour = value.hour,
            initialMinute = value.minute,
            onConfirm = { h, m ->
                onValueChange(LocalTime(h, m))
                showBottomSheet = false
            },
            onDismiss = { showBottomSheet = false }
        )
    }
}

/**
 * ?œê°„ ë¬¸ì?´ì„ ??ë¶„ìœ¼ë¡??Œì‹±?©ë‹ˆ??
 * @param time HH:mm ?•ì‹???œê°„ ë¬¸ì??
 * @return (hour, minute) Pair
 */
private fun parseTime(time: String): Pair<Int, Int> {
    if (time.isBlank()) return Pair(0, 0)
    val parts = time.split(":")
    return if (parts.size == 2) {
        Pair(parts[0].toIntOrNull() ?: 0, parts[1].toIntOrNull() ?: 0)
    } else {
        Pair(0, 0)
    }
}

/**
 * ??ë¶„ì„ HH:mm ?•ì‹ ë¬¸ì?´ë¡œ ?¬ë§·?©ë‹ˆ??
 * @param hour ?œê°„ (0~23)
 * @param minute ë¶?(0~59)
 * @return HH:mm ?•ì‹ ë¬¸ì??
 */
private fun formatTime(hour: Int, minute: Int): String =
    "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"

/**
 * ?„í™”ë²ˆí˜¸ ?¬ë§· VisualTransformation.
 * ?«ìë¥?010-1234-5678 ?•ì‹?¼ë¡œ ?œì‹œ?©ë‹ˆ??
 */
private object PhoneNumberTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text
        if (digits.isEmpty()) return TransformedText(text, OffsetMapping.Identity)

        val formatted = buildString {
            digits.forEachIndexed { index, c ->
                if (index == 3 || index == 7) append('-')
                append(c)
            }
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return when {
                    offset <= 3 -> offset
                    offset <= 7 -> offset + 1
                    else -> offset + 2
                }.coerceAtMost(formatted.length)
            }

            override fun transformedToOriginal(offset: Int): Int {
                return when {
                    offset <= 3 -> offset
                    offset <= 8 -> offset - 1
                    else -> offset - 2
                }.coerceAtMost(digits.length)
            }
        }

        return TransformedText(AnnotatedString(formatted), offsetMapping)
    }
}

/**
 * ?«ìë¥?3?ë¦¬ë§ˆë‹¤ ì½¤ë§ˆë¡?êµ¬ë¶„?˜ëŠ” VisualTransformation.
 * ?ë³¸ ?ìŠ¤?¸ëŠ” ?œìˆ˜ ?«ì?´ë©°, ?œì‹œ ??ì½¤ë§ˆê°€ ?½ì…?©ë‹ˆ??
 */
private object ThousandSeparatorTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val original = text.text
        if (original.isEmpty()) return TransformedText(text, OffsetMapping.Identity)

        val formatted = buildString {
            original.reversed().forEachIndexed { index, c ->
                if (index > 0 && index % 3 == 0) append(',')
                append(c)
            }
        }.reversed()

        // originalOffset[i] = ië²ˆì§¸ ?ë³¸ ë¬¸ì??ë³€?????„ì¹˜
        val originalToTransformedArray = IntArray(original.length + 1)
        var origIdx = 0
        formatted.forEachIndexed { transformedIdx, c ->
            if (c != ',') {
                originalToTransformedArray[origIdx] = transformedIdx
                origIdx++
            }
        }
        originalToTransformedArray[original.length] = formatted.length

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int =
                originalToTransformedArray[offset.coerceIn(0, original.length)]

            override fun transformedToOriginal(offset: Int): Int {
                var count = 0
                for (i in formatted.indices) {
                    if (i >= offset) break
                    if (formatted[i] != ',') count++
                }
                return count.coerceAtMost(original.length)
            }
        }

        return TransformedText(AnnotatedString(formatted), offsetMapping)
    }
}

/**
 * ?ì—… ?œê°„ ?¹ì…˜.
 * @param batchStartTime ?¼ê´„ ?œì‘ ?œê°„
 * @param batchEndTime ?¼ê´„ ì¢…ë£Œ ?œê°„
 * @param daySchedules ?”ì¼ë³??¤ì?ì¤?ëª©ë¡
 * @param onBatchStartTimeChanged ?¼ê´„ ?œì‘ ?œê°„ ë³€ê²?ì½œë°±
 * @param onBatchEndTimeChanged ?¼ê´„ ì¢…ë£Œ ?œê°„ ë³€ê²?ì½œë°±
 * @param onApplyBatchTime ?¼ê´„ ?ìš© ë²„íŠ¼ ?´ë¦­ ì½œë°±
 * @param onDayEnabledChanged ?”ì¼ ?œì„±??ë³€ê²?ì½œë°±
 * @param onDayStartTimeChanged ?”ì¼ ?œì‘ ?œê°„ ë³€ê²?ì½œë°±
 * @param onDayEndTimeChanged ?”ì¼ ì¢…ë£Œ ?œê°„ ë³€ê²?ì½œë°±
 */
@Composable
private fun BusinessHoursSection(
    batchStartTime: LocalTime,
    batchEndTime: LocalTime,
    daySchedules: ImmutableMap<DayOfWeek, DayScheduleModel>,
    onBatchStartTimeChanged: (LocalTime) -> Unit,
    onBatchEndTimeChanged: (LocalTime) -> Unit,
    onApplyBatchTime: () -> Unit,
    onDayEnabledChanged: (DayOfWeek, Boolean) -> Unit,
    onDayStartTimeChanged: (DayOfWeek, LocalTime) -> Unit,
    onDayEndTimeChanged: (DayOfWeek, LocalTime) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
        Text(
            text = "?ì—… ?œê°„",
            style = typography.headingMedium20,
            color = color.gray900
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Batch input card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(color.green50, color.green100)
                    ),
                    shape = RoundedCornerShape(14.dp)
                )
                .border(1.dp, color.green300, RoundedCornerShape(14.dp))
                .padding(17.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "?¼ê´„ ?…ë ¥",
                style = typography.bodyMedium14,
                color = color.gray900
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TimePickerField(
                    value = batchStartTime,
                    onValueChange = onBatchStartTimeChanged,
                    modifier = Modifier.weight(1f),
                    backgroundColor = Color.White
                )
                Text(text = "~", style = typography.bodyRegular14, color = color.gray400)
                TimePickerField(
                    value = batchEndTime,
                    onValueChange = onBatchEndTimeChanged,
                    modifier = Modifier.weight(1f),
                    backgroundColor = Color.White
                )
            }
            Button(
                onClick = onApplyBatchTime,
                modifier = Modifier.fillMaxWidth().height(40.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = color.green500)
            ) {
                Text(
                    text = "ëª¨ë‘ ?ìš©",
                    style = typography.bodyMedium14,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Day schedules
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            daySchedules.forEach { (key, value) ->

                DayScheduleRow(
                    dayOfWeek = key,
                    schedule = value,
                    onEnabledChanged = onDayEnabledChanged,
                    onStartTimeChanged = onDayStartTimeChanged,
                    onEndTimeChanged = onDayEndTimeChanged
                )
            }
        }

    }
}

/**
 * ?”ì¼ë³??ì—… ?œê°„ ??
 * @param schedule ?”ì¼ ?¤ì?ì¤??°ì´??
 * @param onEnabledChanged ?œì„±???íƒœ ë³€ê²?ì½œë°±
 * @param onStartTimeChanged ?œì‘ ?œê°„ ë³€ê²?ì½œë°±
 * @param onEndTimeChanged ì¢…ë£Œ ?œê°„ ë³€ê²?ì½œë°±
 */
@Composable
private fun DayScheduleRow(
    dayOfWeek: DayOfWeek,
    schedule: DayScheduleModel,
    onEnabledChanged: (DayOfWeek, Boolean) -> Unit,
    onStartTimeChanged: (DayOfWeek, LocalTime) -> Unit,
    onEndTimeChanged: (DayOfWeek, LocalTime) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.heightIn(min = 40.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.width(48.dp)
        ) {
            Checkbox(
                checked = schedule.isEnabled,
                onCheckedChange = { onEnabledChanged(dayOfWeek, it) },
                colors = CheckboxDefaults.colors(
                    checkedColor = color.green500,
                    uncheckedColor = color.gray300
                ),
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = dayOfWeek.shortName,
                style = typography.bodyMedium14,
                color = color.gray800
            )
        }
        if (schedule.isEnabled) {
            TimePickerField(
                value = schedule.startTime,
                onValueChange = { onStartTimeChanged(dayOfWeek, it) },
                modifier = Modifier.weight(1f)
            )
            Text(text = "~", style = typography.bodyRegular14, color = color.gray400)
            TimePickerField(
                value = schedule.endTime,
                onValueChange = { onEndTimeChanged(dayOfWeek, it) },
                modifier = Modifier.weight(1f)
            )
        } else {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "?´ë¬´",
                    style = typography.bodyMedium14,
                    color = color.gray400,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

/**
 * ë§¤ì… ?¨ê? ?¹ì…˜.
 * @param priceItems ë§¤ì… ?ˆëª© ëª©ë¡
 * @param onAddPriceItem ?ˆëª© ì¶”ê? ì½œë°±
 * @param onRemoveItem ?ˆëª© ?? œ ì½œë°±
 * @param onNameChanged ?ˆëª©ëª?ë³€ê²?ì½œë°±
 * @param onPriceChanged ?ˆëª© ?¨ê? ë³€ê²?ì½œë°±
 */
@Composable
private fun PriceSection(
    priceItems: ImmutableList<PriceItemModel>,
    onAddPriceItem: () -> Unit,
    onRemoveItem: (Int) -> Unit,
    onNameChanged: (Int, ItemName) -> Unit,
    onPriceChanged: (Int, Int?) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
        Text(
            text = "ë§¤ì… ?¨ê?",
            style = typography.headingMedium20,
            color = color.gray900
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "ì·¨ê¸‰?˜ì‹œ???ˆëª©ê³?kg??ë§¤ì… ?¨ê?ë¥??…ë ¥?´ì£¼?¸ìš”",
            style = typography.bodyRegular14,
            color = color.gray600
        )
        Spacer(modifier = Modifier.height(16.dp))

        priceItems.forEachIndexed { index, item ->
            PriceItemCard(
                item = item,
                onNameChanged = { onNameChanged(index, it) },
                onPriceChanged = { onPriceChanged(index, it) },
                onRemove = { if (priceItems.size > 1) onRemoveItem(index) }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // ?ˆëª© ì¶”ê??˜ê¸° ë²„íŠ¼
        OutlinedButton(
            onClick = onAddPriceItem,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(
                width = 2.dp,
                color = color.gray300
            ),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
        ) {
            Icon(
                painter = painterResource(Res.drawable.icon_24_add),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = color.gray700
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "?ˆëª© ì¶”ê??˜ê¸°",
                style = typography.bodyMedium14,
                color = color.gray700
            )
        }
    }
}

/**
 * ë§¤ì… ?¨ê? ?ˆëª© ì¹´ë“œ.
 * ?ˆëª©ëª??…ë ¥, kg??ë§¤ì…ê°€ ?…ë ¥, ?? œ ë²„íŠ¼???¬í•¨?©ë‹ˆ??
 * @param item ?ˆëª© ?°ì´??
 * @param onNameChanged ?ˆëª©ëª?ë³€ê²?ì½œë°±
 * @param onCustomNameChanged ì§ì ‘ ?…ë ¥ ?ˆëª©ëª?ë³€ê²?ì½œë°±
 * @param onPriceChanged ?¨ê? ë³€ê²?ì½œë°±
 * @param onRemove ?? œ ì½œë°± (null?´ë©´ ?? œ ë²„íŠ¼ ?¨ê?)
 */
@Composable
private fun PriceItemCard(
    item: PriceItemModel,
    onNameChanged: (ItemName) -> Unit = {},
    onPriceChanged: (Int?) -> Unit = {},
    onRemove: (() -> Unit) = {}
) {
    val customNameFocusRequester = remember { FocusRequester() }
    val priceFocusRequester = remember { FocusRequester() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color.gray50.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
            .border(1.dp, color.gray200, RoundedCornerShape(14.dp))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 28.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ?ˆëª©
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "?ˆëª©",
                    style = typography.bodyMedium14,
                    color = color.gray800
                )
                var showPicker by remember { mutableStateOf(false) }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White)
                        .border(1.dp, color.gray200, RoundedCornerShape(10.dp))
                        .clickable { showPicker = true }
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    val name = if (item.name is ItemName.Custom) "ì§ì ‘ ?…ë ¥" else item.name.value
                    Text(
                        text = name,
                        style = typography.bodyRegular16,
                        color = if (item.name == ItemName.None) color.gray500 else color.gray900
                    )
                }
                if (showPicker) {
                    ItemPickerBottomSheet(
                        onItemSelected = {
                            val name = if (it == "ì§ì ‘ ?…ë ¥") ItemName.Custom("") else ItemName.Basic(it)
                            onNameChanged(name)
                        },
                        onDismiss = { showPicker = false }
                    )
                }
            }

            // ì§ì ‘ ?…ë ¥ ?ˆëª© (?ˆëª©??"ì§ì ‘ ?…ë ¥"???Œë§Œ ?œì‹œ)
            if (item.name is ItemName.Custom) {
                LaunchedEffect(Unit) {
                    customNameFocusRequester.requestFocus()
                }
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "ì§ì ‘ ?…ë ¥ ?ˆëª©",
                        style = typography.bodyMedium14,
                        color = color.gray800
                    )
                    BasicTextField(
                        value = item.name.value,
                        onValueChange = { onNameChanged(ItemName.Custom(it)) },
                        singleLine = true,
                        modifier = Modifier.focusRequester(customNameFocusRequester),
                        textStyle = typography.bodyRegular16.copy(color = color.gray900),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(
                            onNext = { priceFocusRequester.requestFocus() }
                        ),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(36.dp)
                                    .background(Color.White, RoundedCornerShape(10.dp))
                                    .border(1.dp, color.gray200, RoundedCornerShape(10.dp))
                                    .padding(horizontal = 12.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                if (item.name.value.isBlank()) {
                                    Text(
                                        text = "?ˆëª©???…ë ¥?´ì£¼?¸ìš”",
                                        style = typography.bodyRegular16,
                                        color = color.gray500
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                }
            }

            // kg??ë§¤ì…ê°€
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "kg??ë§¤ì…ê°€",
                    style = typography.bodyMedium14,
                    color = color.gray800
                )
                BasicTextField(
                    value = item.price?.toString() ?: "",
                    onValueChange = { newValue ->
                        val digits = newValue.filter { it.isDigit() }
                        onPriceChanged(digits.toIntOrNull())
                    },
                    singleLine = true,
                    modifier = Modifier.focusRequester(priceFocusRequester),
                    textStyle = typography.bodyRegular16.copy(color = color.gray900),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    visualTransformation = ThousandSeparatorTransformation,
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(36.dp)
                                .background(Color.White, RoundedCornerShape(8.dp))
                                .border(1.dp, color.gray200, RoundedCornerShape(8.dp))
                                .padding(horizontal = 12.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (item.price == null) {
                                Text(
                                    text = "ë§¤ì… ?¨ê?ë¥??…ë ¥?˜ì„¸??,
                                    style = typography.bodyRegular16,
                                    color = color.gray500
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(modifier = Modifier.weight(1f)) {
                                    innerTextField()
                                }
                                Text(
                                    text = "??/ kg",
                                    style = typography.bodyRegular14,
                                    color = color.gray600
                                )
                            }
                        }
                    }
                )
            }
        }

        // X ?? œ ë²„íŠ¼
        IconButton(
            onClick = onRemove,
            modifier = Modifier
                .size(28.dp)
                .align(Alignment.TopEnd)
        ) {
            Icon(
                painter = painterResource(Res.drawable.icon_24_close),
                contentDescription = "?ˆëª© ?? œ",
                modifier = Modifier.size(16.dp),
                tint = color.gray500
            )
        }
    }
}

/**
 * ?˜ë‹¨ ?ˆë‚´ ë©”ì‹œì§€ ì¹´ë“œ.
 */
@Composable
private fun InfoNotice() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .background(color.green50, RoundedCornerShape(14.dp))
            .border(1.dp, color.green300, RoundedCornerShape(14.dp))
            .padding(17.dp)
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(color.green500, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "!",
                fontSize = 12.sp,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "?±ë¡?˜ì‹  ?•ë³´??ê³ ê°?¤ì—ê²?ê³µê°œ?˜ë©°,\n?¬ëª…??ê±°ë˜ë¥??„í•´ ?•í™•???•ë³´ë¥??…ë ¥?´ì£¼?¸ìš”.",
            style = typography.bodyRegular14.copy(lineHeight = 22.75.sp),
            color = color.gray800
        )
    }
}

/**
 * ?˜ë‹¨ ?±ë¡?˜ê¸° ë²„íŠ¼.
 * @param onClick ë²„íŠ¼ ?´ë¦­ ì½œë°±
 */
@Composable
private fun SubmitButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 17.dp)
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = color.green500)
        ) {
            Text(
                text = "?±ë¡?˜ê¸°",
                style = typography.bodyMedium14,
                color = Color.White
            )
        }
    }
}

/**
 * ?„ì²´ ?…ì²´ ?±ë¡ ?”ë©´ ë¯¸ë¦¬ë³´ê¸°.
 */
@Preview(showBackground = true)
@Composable
private fun RegistStoreScreenPreview() {
    RebornTheme {
        RegistStoreScreen(
            model = RegistStoreModel(),
            addressState = false
        )
    }
}

/**
 * ?¬ì§„ ?±ë¡ ?¹ì…˜ ë¯¸ë¦¬ë³´ê¸°.
 */
@Preview(showBackground = true)
@Composable
private fun PhotoSectionPreview() {
    RebornTheme {
        PhotoSection(
            photos = persistentListOf(),
            onPhotosAdded = {},
            onPhotoRemoved = {}
        )
    }
}

/**
 * ê¸°ë³¸ ?•ë³´ ?¹ì…˜ ë¯¸ë¦¬ë³´ê¸°.
 */
@Preview(showBackground = true)
@Composable
private fun BasicInfoSectionPreview() {
    RebornTheme {
        BasicInfoSection(
            storeName = "?¬í™œ??ê³ ë¬¼??,
            phone = "01012345678",
            address = "?œìš¸??ê°•ë‚¨êµ??Œí—¤?€ë¡?123",
            description = "20??ê²½ë ¥??? ë¢°?????ˆëŠ” ê³ ë¬¼?ì…?ˆë‹¤.",
            isShowingAddressSearch = false,
            onStoreNameChanged = {},
            onPhoneChanged = {},
            onAddressChanged = {},
            onDescriptionChanged = {},
            requestAddressSearchState = {}
        )
    }
}

/**
 * ?ì—… ?œê°„ ?¹ì…˜ ë¯¸ë¦¬ë³´ê¸°.
 */
@Preview(showBackground = true)
@Composable
private fun BusinessHoursSectionPreview() {
    RebornTheme {
        BusinessHoursSection(
            batchStartTime = LocalTime(9, 0),
            batchEndTime = LocalTime(18, 0),
            daySchedules = DayOfWeek.entries
                .associateWith { DayScheduleModel() }
                .toPersistentHashMap(),
            onBatchStartTimeChanged = {},
            onBatchEndTimeChanged = {},
            onApplyBatchTime = {},
            onDayEnabledChanged = { _, _ -> },
            onDayStartTimeChanged = { _, _ -> },
            onDayEndTimeChanged = { _, _ -> }
        )
    }
}

/**
 * ë§¤ì… ?¨ê? ?¹ì…˜ ë¯¸ë¦¬ë³´ê¸° (?ˆëª© ?ˆìŒ).
 */
@Preview(showBackground = true)
@Composable
private fun PriceSectionPreview() {
    RebornTheme {
        PriceSection(
            priceItems = persistentListOf(
                PriceItemModel(name = ItemName.Basic("êµ¬ë¦¬"), price = 8500),
                PriceItemModel(name = ItemName.Custom("ê¸°íŒ"), price = null)
            ),
            onAddPriceItem = {},
            onRemoveItem = {},
            onNameChanged = { _, _ -> },
            onPriceChanged = { _, _ -> }
        )
    }
}

/**
 * ë§¤ì… ?¨ê? ?ˆëª© ì¹´ë“œ ë¯¸ë¦¬ë³´ê¸°.
 */
@Preview(showBackground = true)
@Composable
private fun PriceItemCardPreview() {
    RebornTheme {
        PriceItemCard(
            item = PriceItemModel(name = ItemName.Custom("ë¬˜ì‚¬"), price = 8500),
            onNameChanged = {},
            onPriceChanged = {},
            onRemove = {}
        )
    }
}

/**
 * ?˜ë‹¨ ?ˆë‚´ ë©”ì‹œì§€ ë¯¸ë¦¬ë³´ê¸°.
 */
@Preview(showBackground = true)
@Composable
private fun InfoNoticePreview() {
    RebornTheme {
        InfoNotice()
    }
}

/**
 * ?±ë¡?˜ê¸° ë²„íŠ¼ ë¯¸ë¦¬ë³´ê¸°.
 */
@Preview(showBackground = true)
@Composable
private fun SubmitButtonPreview() {
    RebornTheme {
        SubmitButton(onClick = {})
    }
}
