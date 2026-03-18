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
import reborn.composeapp.generated.resources.ic_add
import reborn.composeapp.generated.resources.ic_back
import reborn.composeapp.generated.resources.ic_camera
import reborn.composeapp.generated.resources.ic_close
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
 * 업체 등록 화면.
 * 사진, 기본 정보, 영업 시간, 매입 단가를 입력하여 업체를 등록합니다.
 */
@Serializable
object RegistStoreScreen : NavKey {

    /**
     * 업체 등록 화면의 UI 이벤트.
     */
    sealed interface UiEvent {

        /** 사진 추가 */
        data class PhotosAdded(val photos: List<ByteArray>) : UiEvent

        /** 사진 삭제 */
        data class PhotoRemoved(val index: Int) : UiEvent

        /** 업체명 변경 */
        data class StoreNameChanged(val name: String) : UiEvent

        /** 전화번호 변경 */
        data class PhoneChanged(val phone: String) : UiEvent

        /** 주소 변경 */
        data class AddressChanged(val address: String) : UiEvent

        /** 업체 소개 변경 */
        data class DescriptionChanged(val description: String) : UiEvent

        /** 일괄 시작 시간 변경 */
        data class BatchStartTimeChanged(val time: LocalTime) : UiEvent

        /** 일괄 종료 시간 변경 */
        data class BatchEndTimeChanged(val time: LocalTime) : UiEvent

        /** 일괄 시간 적용 */
        data object ApplyBatchTime : UiEvent

        /** 요일 활성화 상태 변경 */
        data class DayEnabledChanged(val key: DayOfWeek, val enabled: Boolean) : UiEvent

        /** 요일별 시작 시간 변경 */
        data class DayStartTimeChanged(val key: DayOfWeek, val time: LocalTime) : UiEvent

        /** 요일별 종료 시간 변경 */
        data class DayEndTimeChanged(val key: DayOfWeek, val time: LocalTime) : UiEvent

        /** 품목 추가 */
        data object AddPriceItem : UiEvent

        /** 품목 삭제 */
        data class RemovePriceItem(val index: Int) : UiEvent

        /** 품목명 변경 */
        data class PriceItemNameChanged(val index: Int, val name: ItemName) : UiEvent

        /** 품목 단가 변경 */
        data class PriceItemPriceChanged(val index: Int, val price: Int?) : UiEvent

        /** 주소 검색 다이얼로그 표시 상태 변경 */
        data class AddressSearchState(val isShow: Boolean) : UiEvent

        /** 등록하기 클릭 */
        data object SubmitClicked : UiEvent
    }

    /**
     * Stateful Wrapper. Koin ViewModel을 주입받고 이벤트를 처리합니다.
     * @param viewModel Koin에서 주입받는 ViewModel
     * @param onBack 뒤로가기 콜백
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
     * Stateless UI. 순수 Composable로 UI를 그립니다.
     * @param model 현재 UI 상태
     * @param snackbarHostState 스낵바 호스트 상태
     * @param onEvent UI 이벤트 콜백
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
                            text = "업체 등록",
                            style = typography.headingMedium18,
                            color = color.gray900
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_back),
                                contentDescription = "뒤로가기",
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
 * 사진 등록 섹션.
 * 갤러리에서 이미지를 선택하고 선택된 이미지를 썸네일로 표시합니다.
 * @param photos 현재 등록된 사진 ByteArray 목록
 * @param maxPhotoCount 최대 등록 가능 사진 수
 * @param onPhotosAdded 사진 추가 콜백
 * @param onPhotoRemoved 사진 삭제 콜백
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
            text = "사진 등록",
            style = typography.headingMedium20,
            color = color.gray900
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "최대 ${maxPhotoCount}장까지 등록 가능합니다",
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
                // 선택된 사진 썸네일
                itemsIndexed(photos) { index, photoBytes ->
                    PhotoThumbnail(
                        photoBytes = photoBytes,
                        onRemove = { onPhotoRemoved(index) }
                    )
                }

                // 추가 버튼 (사진이 최대 수 미만일 때만 표시)
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
                                    painter = painterResource(Res.drawable.ic_camera),
                                    contentDescription = "사진 추가",
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
 * 사진 썸네일. 선택된 이미지를 표시하고 삭제 버튼을 제공합니다.
 * @param photoBytes 이미지 ByteArray
 * @param onRemove 삭제 콜백
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
            contentDescription = "등록된 사진",
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
                painter = painterResource(Res.drawable.ic_close),
                contentDescription = "사진 삭제",
                modifier = Modifier.size(10.dp),
                tint = Color.White
            )
        }
    }
}

/**
 * 섹션 구분선.
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
 * 기본 정보 섹션.
 * @param storeName 업체명
 * @param phone 전화번호
 * @param address 주소
 * @param description 업체 소개
 * @param isShowingAddressSearch 주소 검색 다이얼로그 표시 여부
 * @param onStoreNameChanged 업체명 변경 콜백
 * @param onPhoneChanged 전화번호 변경 콜백
 * @param onAddressChanged 주소 변경 콜백
 * @param onDescriptionChanged 업체 소개 변경 콜백
 * @param requestAddressSearchState 주소 검색 다이얼로그 표시 상태 변경 요청 콜백
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
            text = "기본 정보",
            style = typography.headingMedium20,
            color = color.gray900
        )
        Spacer(modifier = Modifier.height(16.dp))

        // 업체명
        RequiredLabel("업체명")
        Spacer(modifier = Modifier.height(8.dp))
        FormTextField(
            value = storeName,
            onValueChange = onStoreNameChanged,
            placeholder = "업체명을 입력해주세요",
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { phoneFocusRequester.requestFocus() }
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 전화번호
        RequiredLabel("전화번호")
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
                            text = "전화번호를 입력해주세요",
                            style = typography.bodyRegular16,
                            color = color.gray500
                        )
                    }
                    innerTextField()
                }
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 주소
        RequiredLabel("주소")
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
                text = address.ifEmpty { "주소를 검색해주세요" },
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

        // 업체 소개
        Text(
            text = "업체 소개",
            style = typography.bodyMedium14,
            color = color.gray800
        )
        Spacer(modifier = Modifier.height(8.dp))
        FormTextField(
            value = description,
            onValueChange = onDescriptionChanged,
            placeholder = "업체를 소개해주세요\n예) 20년 경력의 신뢰할 수 있는 고물상입니다.",
            minHeight = 120,
            singleLine = false,
            modifier = Modifier.focusRequester(descriptionFocusRequester),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default)
        )
    }
}

/**
 * 필수 입력 라벨 (이름 + 빨간 별표).
 * @param text 라벨 텍스트
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
 * 공통 입력 필드.
 * @param value 현재 값
 * @param onValueChange 값 변경 콜백
 * @param placeholder 플레이스홀더 텍스트
 * @param minHeight 최소 높이 (dp)
 * @param singleLine 한 줄 입력 여부
 * @param modifier Modifier
 * @param keyboardOptions 키보드 옵션
 * @param keyboardActions 키보드 액션
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
 * 시간 선택 필드. 클릭 시 TimePickerBottomSheet를 표시합니다.
 * @param value 현재 시간 값
 * @param onValueChange 시간 변경 콜백
 * @param modifier Modifier
 * @param backgroundColor 배경색
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
 * 시간 문자열을 시/분으로 파싱합니다.
 * @param time HH:mm 형식의 시간 문자열
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
 * 시/분을 HH:mm 형식 문자열로 포맷합니다.
 * @param hour 시간 (0~23)
 * @param minute 분 (0~59)
 * @return HH:mm 형식 문자열
 */
private fun formatTime(hour: Int, minute: Int): String =
    "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"

/**
 * 전화번호 포맷 VisualTransformation.
 * 숫자를 010-1234-5678 형식으로 표시합니다.
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
 * 숫자를 3자리마다 콤마로 구분하는 VisualTransformation.
 * 원본 텍스트는 순수 숫자이며, 표시 시 콤마가 삽입됩니다.
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

        // originalOffset[i] = i번째 원본 문자의 변환 후 위치
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
 * 영업 시간 섹션.
 * @param batchStartTime 일괄 시작 시간
 * @param batchEndTime 일괄 종료 시간
 * @param daySchedules 요일별 스케줄 목록
 * @param onBatchStartTimeChanged 일괄 시작 시간 변경 콜백
 * @param onBatchEndTimeChanged 일괄 종료 시간 변경 콜백
 * @param onApplyBatchTime 일괄 적용 버튼 클릭 콜백
 * @param onDayEnabledChanged 요일 활성화 변경 콜백
 * @param onDayStartTimeChanged 요일 시작 시간 변경 콜백
 * @param onDayEndTimeChanged 요일 종료 시간 변경 콜백
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
            text = "영업 시간",
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
                text = "일괄 입력",
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
                    text = "모두 적용",
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
 * 요일별 영업 시간 행.
 * @param schedule 요일 스케줄 데이터
 * @param onEnabledChanged 활성화 상태 변경 콜백
 * @param onStartTimeChanged 시작 시간 변경 콜백
 * @param onEndTimeChanged 종료 시간 변경 콜백
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
                    text = "휴무",
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
 * 매입 단가 섹션.
 * @param priceItems 매입 품목 목록
 * @param onAddPriceItem 품목 추가 콜백
 * @param onRemoveItem 품목 삭제 콜백
 * @param onNameChanged 품목명 변경 콜백
 * @param onPriceChanged 품목 단가 변경 콜백
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
            text = "매입 단가",
            style = typography.headingMedium20,
            color = color.gray900
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "취급하시는 품목과 kg당 매입 단가를 입력해주세요",
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

        // 품목 추가하기 버튼
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
                painter = painterResource(Res.drawable.ic_add),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = color.gray700
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "품목 추가하기",
                style = typography.bodyMedium14,
                color = color.gray700
            )
        }
    }
}

/**
 * 매입 단가 품목 카드.
 * 품목명 입력, kg당 매입가 입력, 삭제 버튼을 포함합니다.
 * @param item 품목 데이터
 * @param onNameChanged 품목명 변경 콜백
 * @param onCustomNameChanged 직접 입력 품목명 변경 콜백
 * @param onPriceChanged 단가 변경 콜백
 * @param onRemove 삭제 콜백 (null이면 삭제 버튼 숨김)
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
            // 품목
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "품목",
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
                    val name = if (item.name is ItemName.Custom) "직접 입력" else item.name.value
                    Text(
                        text = name,
                        style = typography.bodyRegular16,
                        color = if (item.name == ItemName.None) color.gray500 else color.gray900
                    )
                }
                if (showPicker) {
                    ItemPickerBottomSheet(
                        onItemSelected = {
                            val name = if (it == "직접 입력") ItemName.Custom("") else ItemName.Basic(it)
                            onNameChanged(name)
                        },
                        onDismiss = { showPicker = false }
                    )
                }
            }

            // 직접 입력 품목 (품목이 "직접 입력"일 때만 표시)
            if (item.name is ItemName.Custom) {
                LaunchedEffect(Unit) {
                    customNameFocusRequester.requestFocus()
                }
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "직접 입력 품목",
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
                                        text = "품목을 입력해주세요",
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

            // kg당 매입가
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "kg당 매입가",
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
                                    text = "매입 단가를 입력하세요",
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
                                    text = "원 / kg",
                                    style = typography.bodyRegular14,
                                    color = color.gray600
                                )
                            }
                        }
                    }
                )
            }
        }

        // X 삭제 버튼
        IconButton(
            onClick = onRemove,
            modifier = Modifier
                .size(28.dp)
                .align(Alignment.TopEnd)
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_close),
                contentDescription = "품목 삭제",
                modifier = Modifier.size(16.dp),
                tint = color.gray500
            )
        }
    }
}

/**
 * 하단 안내 메시지 카드.
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
            text = "등록하신 정보는 고객들에게 공개되며,\n투명한 거래를 위해 정확한 정보를 입력해주세요.",
            style = typography.bodyRegular14.copy(lineHeight = 22.75.sp),
            color = color.gray800
        )
    }
}

/**
 * 하단 등록하기 버튼.
 * @param onClick 버튼 클릭 콜백
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
                text = "등록하기",
                style = typography.bodyMedium14,
                color = Color.White
            )
        }
    }
}

/**
 * 전체 업체 등록 화면 미리보기.
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
 * 사진 등록 섹션 미리보기.
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
 * 기본 정보 섹션 미리보기.
 */
@Preview(showBackground = true)
@Composable
private fun BasicInfoSectionPreview() {
    RebornTheme {
        BasicInfoSection(
            storeName = "재활용 고물상",
            phone = "01012345678",
            address = "서울시 강남구 테헤란로 123",
            description = "20년 경력의 신뢰할 수 있는 고물상입니다.",
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
 * 영업 시간 섹션 미리보기.
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
 * 매입 단가 섹션 미리보기 (품목 있음).
 */
@Preview(showBackground = true)
@Composable
private fun PriceSectionPreview() {
    RebornTheme {
        PriceSection(
            priceItems = persistentListOf(
                PriceItemModel(name = ItemName.Basic("구리"), price = 8500),
                PriceItemModel(name = ItemName.Custom("기판"), price = null)
            ),
            onAddPriceItem = {},
            onRemoveItem = {},
            onNameChanged = { _, _ -> },
            onPriceChanged = { _, _ -> }
        )
    }
}

/**
 * 매입 단가 품목 카드 미리보기.
 */
@Preview(showBackground = true)
@Composable
private fun PriceItemCardPreview() {
    RebornTheme {
        PriceItemCard(
            item = PriceItemModel(name = ItemName.Custom("묘사"), price = 8500),
            onNameChanged = {},
            onPriceChanged = {},
            onRemove = {}
        )
    }
}

/**
 * 하단 안내 메시지 미리보기.
 */
@Preview(showBackground = true)
@Composable
private fun InfoNoticePreview() {
    RebornTheme {
        InfoNotice()
    }
}

/**
 * 등록하기 버튼 미리보기.
 */
@Preview(showBackground = true)
@Composable
private fun SubmitButtonPreview() {
    RebornTheme {
        SubmitButton(onClick = {})
    }
}
