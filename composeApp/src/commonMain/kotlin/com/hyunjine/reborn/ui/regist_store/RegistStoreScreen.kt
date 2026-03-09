package com.hyunjine.reborn.ui.regist_store

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import kotlinx.collections.immutable.ImmutableList
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import reborn.composeapp.generated.resources.Res
import reborn.composeapp.generated.resources.ic_add
import reborn.composeapp.generated.resources.ic_back
import reborn.composeapp.generated.resources.ic_camera

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
        /** 뒤로가기 */
        data object BackClicked : UiEvent

        /** 사진 추가 클릭 */
        data object AddPhotoClicked : UiEvent

        /** 업체명 변경 */
        data class StoreNameChanged(val name: String) : UiEvent

        /** 주소 변경 */
        data class AddressChanged(val address: String) : UiEvent

        /** 업체 소개 변경 */
        data class DescriptionChanged(val description: String) : UiEvent

        /** 일괄 시작 시간 변경 */
        data class BatchStartTimeChanged(val time: String) : UiEvent

        /** 일괄 종료 시간 변경 */
        data class BatchEndTimeChanged(val time: String) : UiEvent

        /** 일괄 시간 적용 */
        data object ApplyBatchTime : UiEvent

        /** 요일 활성화 상태 변경 */
        data class DayEnabledChanged(val index: Int, val enabled: Boolean) : UiEvent

        /** 요일별 시작 시간 변경 */
        data class DayStartTimeChanged(val index: Int, val time: String) : UiEvent

        /** 요일별 종료 시간 변경 */
        data class DayEndTimeChanged(val index: Int, val time: String) : UiEvent

        /** 공휴일 휴무 변경 */
        data class HolidayClosedChanged(val closed: Boolean) : UiEvent

        /** 품목 추가 */
        data object AddPriceItem : UiEvent

        /** 품목명 변경 */
        data class PriceItemNameChanged(val index: Int, val name: String) : UiEvent

        /** 품목 단가 변경 */
        data class PriceItemPriceChanged(val index: Int, val price: String) : UiEvent

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
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        invoke(
            uiState = uiState,
            onEvent = { event ->
                when (event) {
                    is UiEvent.BackClicked -> onBack()
                    else -> viewModel.event(event)
                }
            }
        )
    }

    /**
     * Stateless UI. 순수 Composable로 UI를 그립니다.
     * @param uiState 현재 UI 상태
     * @param onEvent UI 이벤트 콜백
     */
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    operator fun invoke(
        uiState: RegistStoreModel,
        onEvent: (UiEvent) -> Unit = {}
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "업체 등록",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF101828)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { onEvent(UiEvent.BackClicked) }) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_back),
                                contentDescription = "뒤로가기",
                                modifier = Modifier.size(24.dp),
                                tint = Color(0xFF101828)
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
                    .verticalScroll(rememberScrollState())
            ) {
                PhotoSection(
                    photoCount = uiState.photoCount,
                    maxPhotoCount = uiState.maxPhotoCount,
                    onAddPhoto = { onEvent(UiEvent.AddPhotoClicked) }
                )
                SectionDivider()
                BasicInfoSection(
                    storeName = uiState.storeName,
                    address = uiState.address,
                    description = uiState.description,
                    onStoreNameChanged = { onEvent(UiEvent.StoreNameChanged(it)) },
                    onAddressChanged = { onEvent(UiEvent.AddressChanged(it)) },
                    onDescriptionChanged = { onEvent(UiEvent.DescriptionChanged(it)) }
                )
                SectionDivider()
                BusinessHoursSection(
                    batchStartTime = uiState.batchStartTime,
                    batchEndTime = uiState.batchEndTime,
                    daySchedules = uiState.daySchedules,
                    isHolidayClosed = uiState.isHolidayClosed,
                    onBatchStartTimeChanged = { onEvent(UiEvent.BatchStartTimeChanged(it)) },
                    onBatchEndTimeChanged = { onEvent(UiEvent.BatchEndTimeChanged(it)) },
                    onApplyBatchTime = { onEvent(UiEvent.ApplyBatchTime) },
                    onDayEnabledChanged = { i, e -> onEvent(UiEvent.DayEnabledChanged(i, e)) },
                    onDayStartTimeChanged = { i, t -> onEvent(UiEvent.DayStartTimeChanged(i, t)) },
                    onDayEndTimeChanged = { i, t -> onEvent(UiEvent.DayEndTimeChanged(i, t)) },
                    onHolidayClosedChanged = { onEvent(UiEvent.HolidayClosedChanged(it)) }
                )
                SectionDivider()
                PriceSection(
                    priceItems = uiState.priceItems,
                    onAddPriceItem = { onEvent(UiEvent.AddPriceItem) },
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
 * @param photoCount 현재 등록된 사진 수
 * @param maxPhotoCount 최대 등록 가능 사진 수
 * @param onAddPhoto 사진 추가 버튼 클릭 콜백
 */
@Composable
private fun PhotoSection(
    photoCount: Int,
    maxPhotoCount: Int,
    onAddPhoto: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
        Text(
            text = "사진 등록",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF101828)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "최대 ${maxPhotoCount}장까지 등록 가능합니다",
            fontSize = 14.sp,
            color = Color(0xFF6A7282)
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(
            onClick = onAddPhoto,
            modifier = Modifier.size(96.dp),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(2.dp, Color(0xFFD1D5DC)),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFFF9FAFB))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_camera),
                    contentDescription = "사진 추가",
                    modifier = Modifier.size(24.dp),
                    tint = Color(0xFF99A1AF)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "$photoCount/$maxPhotoCount",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF6A7282)
                )
            }
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
            .background(Color(0xFFF8F9FA))
    )
}

/**
 * 기본 정보 섹션.
 * @param storeName 업체명
 * @param address 주소
 * @param description 업체 소개
 * @param onStoreNameChanged 업체명 변경 콜백
 * @param onAddressChanged 주소 변경 콜백
 * @param onDescriptionChanged 업체 소개 변경 콜백
 */
@Composable
private fun BasicInfoSection(
    storeName: String,
    address: String,
    description: String,
    onStoreNameChanged: (String) -> Unit,
    onAddressChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
        Text(
            text = "기본 정보",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF101828)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // 업체명
        RequiredLabel("업체명")
        Spacer(modifier = Modifier.height(8.dp))
        FormTextField(
            value = storeName,
            onValueChange = onStoreNameChanged,
            placeholder = "업체명을 입력해주세요"
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 주소
        RequiredLabel("주소")
        Spacer(modifier = Modifier.height(8.dp))
        FormTextField(
            value = address,
            onValueChange = onAddressChanged,
            placeholder = "주소를 검색해주세요"
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 업체 소개
        Text(
            text = "업체 소개",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF364153)
        )
        Spacer(modifier = Modifier.height(8.dp))
        FormTextField(
            value = description,
            onValueChange = onDescriptionChanged,
            placeholder = "업체를 소개해주세요\n예) 20년 경력의 신뢰할 수 있는 고물상입니다.",
            minHeight = 120,
            singleLine = false
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
            withStyle(SpanStyle(color = Color(0xFFFB2C36))) {
                append("*")
            }
        },
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = Color(0xFF364153)
    )
}

/**
 * 공통 입력 필드.
 * @param value 현재 값
 * @param onValueChange 값 변경 콜백
 * @param placeholder 플레이스홀더 텍스트
 * @param minHeight 최소 높이 (dp)
 * @param singleLine 한 줄 입력 여부
 */
@Composable
private fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    minHeight: Int = 48,
    singleLine: Boolean = true
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = singleLine,
        textStyle = TextStyle(fontSize = 16.sp, color = Color(0xFF101828)),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(minHeight.dp)
                    .background(Color(0xFFF9FAFB), RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = if (singleLine) 0.dp else 8.dp),
                contentAlignment = if (singleLine) Alignment.CenterStart else Alignment.TopStart
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        fontSize = 16.sp,
                        color = Color(0xFF717182)
                    )
                }
                innerTextField()
            }
        }
    )
}

/**
 * 시간 입력 필드.
 * @param value 현재 값
 * @param onValueChange 값 변경 콜백
 * @param modifier Modifier
 */
@Composable
private fun TimeTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        textStyle = TextStyle(fontSize = 14.sp, color = Color(0xFF101828)),
        decorationBox = { innerTextField ->
            Box(
                modifier = modifier
                    .height(39.dp)
                    .background(Color(0xFFF9FAFB), RoundedCornerShape(10.dp))
                    .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                innerTextField()
            }
        }
    )
}

/**
 * 영업 시간 섹션.
 * @param batchStartTime 일괄 시작 시간
 * @param batchEndTime 일괄 종료 시간
 * @param daySchedules 요일별 스케줄 목록
 * @param isHolidayClosed 공휴일 휴무 여부
 * @param onBatchStartTimeChanged 일괄 시작 시간 변경 콜백
 * @param onBatchEndTimeChanged 일괄 종료 시간 변경 콜백
 * @param onApplyBatchTime 일괄 적용 버튼 클릭 콜백
 * @param onDayEnabledChanged 요일 활성화 변경 콜백
 * @param onDayStartTimeChanged 요일 시작 시간 변경 콜백
 * @param onDayEndTimeChanged 요일 종료 시간 변경 콜백
 * @param onHolidayClosedChanged 공휴일 휴무 변경 콜백
 */
@Composable
private fun BusinessHoursSection(
    batchStartTime: String,
    batchEndTime: String,
    daySchedules: ImmutableList<DayScheduleModel>,
    isHolidayClosed: Boolean,
    onBatchStartTimeChanged: (String) -> Unit,
    onBatchEndTimeChanged: (String) -> Unit,
    onApplyBatchTime: () -> Unit,
    onDayEnabledChanged: (Int, Boolean) -> Unit,
    onDayStartTimeChanged: (Int, String) -> Unit,
    onDayEndTimeChanged: (Int, String) -> Unit,
    onHolidayClosedChanged: (Boolean) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
        Text(
            text = "영업 시간",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF101828)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Batch input card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFFF0FDF4), Color(0xFFECFDF5))
                    ),
                    shape = RoundedCornerShape(14.dp)
                )
                .border(1.dp, Color(0xFFB9F8CF), RoundedCornerShape(14.dp))
                .padding(17.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "일괄 입력",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF101828)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BasicTextField(
                    value = batchStartTime,
                    onValueChange = onBatchStartTimeChanged,
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 14.sp, color = Color(0xFF101828)),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(43.dp)
                                .background(Color.White, RoundedCornerShape(10.dp))
                                .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(10.dp))
                                .padding(horizontal = 12.dp),
                            contentAlignment = Alignment.Center
                        ) { innerTextField() }
                    }
                )
                Text(text = "~", fontSize = 14.sp, color = Color(0xFF99A1AF))
                BasicTextField(
                    value = batchEndTime,
                    onValueChange = onBatchEndTimeChanged,
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 14.sp, color = Color(0xFF101828)),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(43.dp)
                                .background(Color.White, RoundedCornerShape(10.dp))
                                .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(10.dp))
                                .padding(horizontal = 12.dp),
                            contentAlignment = Alignment.Center
                        ) { innerTextField() }
                    }
                )
            }
            Button(
                onClick = onApplyBatchTime,
                modifier = Modifier.fillMaxWidth().height(40.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF22C55E))
            ) {
                Text(
                    text = "모두 적용",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Day schedules
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            daySchedules.forEachIndexed { index, schedule ->
                DayScheduleRow(
                    schedule = schedule,
                    onEnabledChanged = { onDayEnabledChanged(index, it) },
                    onStartTimeChanged = { onDayStartTimeChanged(index, it) },
                    onEndTimeChanged = { onDayEndTimeChanged(index, it) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Holiday closed checkbox
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = isHolidayClosed,
                onCheckedChange = onHolidayClosedChanged,
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF22C55E),
                    uncheckedColor = Color(0xFFD1D5DC)
                ),
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "공휴일 휴무",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF364153)
            )
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
    schedule: DayScheduleModel,
    onEnabledChanged: (Boolean) -> Unit,
    onStartTimeChanged: (String) -> Unit,
    onEndTimeChanged: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.width(48.dp)
        ) {
            Checkbox(
                checked = schedule.isEnabled,
                onCheckedChange = onEnabledChanged,
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF22C55E),
                    uncheckedColor = Color(0xFFD1D5DC)
                ),
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = schedule.day,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF364153)
            )
        }
        TimeTextField(
            value = schedule.startTime,
            onValueChange = onStartTimeChanged,
            modifier = Modifier.weight(1f)
        )
        Text(text = "~", fontSize = 14.sp, color = Color(0xFF99A1AF))
        TimeTextField(
            value = schedule.endTime,
            onValueChange = onEndTimeChanged,
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * 매입 단가 섹션.
 * @param priceItems 매입 품목 목록
 * @param onAddPriceItem 품목 추가 콜백
 * @param onNameChanged 품목명 변경 콜백
 * @param onPriceChanged 품목 단가 변경 콜백
 */
@Composable
private fun PriceSection(
    priceItems: ImmutableList<PriceItemModel>,
    onAddPriceItem: () -> Unit,
    onNameChanged: (Int, String) -> Unit,
    onPriceChanged: (Int, String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
        Text(
            text = "매입 단가",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF101828)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "취급하시는 품목과 kg당 매입 단가를 입력해주세요",
            fontSize = 14.sp,
            color = Color(0xFF6A7282)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Added price items
        priceItems.forEachIndexed { index, item ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FormTextField(
                    value = item.name,
                    onValueChange = { onNameChanged(index, it) },
                    placeholder = "품목명"
                )
            }
        }

        // Add button
        OutlinedButton(
            onClick = onAddPriceItem,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(2.dp, Color(0xFFD1D5DC)),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_add),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Color(0xFF4A5565)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "품목 추가하기",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF4A5565)
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
            .background(Color(0xFFF0FDF4), RoundedCornerShape(14.dp))
            .border(1.dp, Color(0xFFB9F8CF), RoundedCornerShape(14.dp))
            .padding(17.dp)
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(Color(0xFF22C55E), CircleShape),
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
            fontSize = 14.sp,
            color = Color(0xFF364153),
            lineHeight = 22.75.sp
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
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF22C55E))
        ) {
            Text(
                text = "등록하기",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }
    }
}

@Preview
@Composable
fun RegistStoreScreenPreview() {
    RegistStoreScreen(
        uiState = RegistStoreModel()
    )
}
