package com.hyunjine.reborn.ui.store_detail

import androidx.compose.foundation.background
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import com.hyunjine.reborn.common.theme.RebornTheme
import com.hyunjine.reborn.common.theme.color
import com.hyunjine.reborn.common.util.fullName
import com.hyunjine.reborn.common.util.shortName
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.DayOfWeek
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import reborn.composeapp.generated.resources.Res
import reborn.composeapp.generated.resources.ic_back
import reborn.composeapp.generated.resources.ic_copy
import reborn.composeapp.generated.resources.ic_location_pin

/**
 * 업체 상세 페이지 화면.
 * 업체의 기본 정보, 영업 시간, 실시간 매입 시세를 표시합니다.
 * @param storeId 업체 ID
 */
@Serializable
data class StoreDetailScreen(
    val storeId: Long
) : NavKey {

    /**
     * 업체 상세 페이지의 UI 이벤트.
     */
    sealed interface UiEvent {
        /** 뒤로가기 버튼 클릭 */
        data object BackClicked : UiEvent

        /** 주소 복사 버튼 클릭 */
        data object CopyAddressClicked : UiEvent

        /** 전화 문의하기 버튼 클릭 */
        data object CallClicked : UiEvent
    }

    /**
     * Stateful Wrapper. Koin ViewModel을 주입받고 이벤트를 처리합니다.
     * @param viewModel Koin에서 주입받는 ViewModel
     * @param onBack 뒤로가기 콜백
     */
    @Composable
    operator fun invoke(
        viewModel: StoreDetailViewModel = koinViewModel { parametersOf(storeId) },
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
    @Composable
    operator fun invoke(
        uiState: StoreDetailModel,
        onEvent: (UiEvent) -> Unit = {}
    ) {
        Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 100.dp)
            ) {
                StoreImageSection(onBackClick = { onEvent(UiEvent.BackClicked) })
                StoreInfoSection(
                    name = uiState.name,
                    address = uiState.address,
                    onCopyClick = { onEvent(UiEvent.CopyAddressClicked) }
                )
                HorizontalDivider(color = color.gray100)
                StoreDescriptionSection(
                    description = uiState.description,
                    businessHours = uiState.businessHours
                )
                HorizontalDivider(color = color.gray100)
                StorePriceSection(
                    prices = uiState.prices,
                    lastUpdated = uiState.lastUpdated
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
 * 업체 이미지 섹션. 이미지 캐러셀과 뒤로가기 버튼, 페이지 인디케이터를 표시합니다.
 * @param onBackClick 뒤로가기 버튼 클릭 콜백
 */
@Composable
private fun StoreImageSection(onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(236.dp)
            .background(color.gray100)
    ) {
        // Back button
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .statusBarsPadding()
                .padding(start = 8.dp, top = 8.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_back),
                contentDescription = "뒤로가기",
                modifier = Modifier.size(24.dp),
                tint = color.gray900
            )
        }

        // Page indicators
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(24.dp)
                    .height(6.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.6f))
            )
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.6f))
            )
        }
    }
}

/**
 * 업체 기본 정보 섹션. 업체명, 인증 배지, 주소를 표시합니다.
 * @param name 업체명
 * @param address 업체 주소
 * @param onCopyClick 주소 복사 버튼 클릭 콜백
 */
@Composable
private fun StoreInfoSection(
    name: String,
    address: String,
    onCopyClick: () -> Unit
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
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = color.gray900
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Address with inline copy icon
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(Res.drawable.ic_location_pin),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = color.gray400
            )
            Spacer(modifier = Modifier.width(8.dp))

            val copyIconId = "copy_icon"
            val annotatedAddress = buildAnnotatedString {
                append("$address ")
                appendInlineContent(copyIconId, "[복사]")
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
                        painter = painterResource(Res.drawable.ic_copy),
                        contentDescription = "주소 복사",
                        modifier = Modifier.size(16.dp),
                        tint = color.gray400
                    )
                }
            )
            Text(
                text = annotatedAddress,
                inlineContent = inlineContent,
                fontSize = 16.sp,
                color = color.gray800,
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClick = onCopyClick)
            )
        }
    }
}

/**
 * 업체 소개 및 영업 시간 섹션.
 * @param description 업체 소개 텍스트
 * @param businessHours 영업 시간 목록
 */
@Composable
private fun StoreDescriptionSection(
    description: String,
    businessHours: ImmutableList<BusinessHourModel>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        // Section title
        Text(
            text = "업체 소개",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = color.gray900
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Description
        Text(
            text = description,
            fontSize = 16.sp,
            color = color.gray800,
            lineHeight = 26.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Business hours title
        Text(
            text = "영업 시간",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = color.gray900
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Business hours list
        Column(
            modifier = Modifier.padding(start = 28.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            businessHours.forEach { hour ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = hour.dayOfWeek.fullName,
                        fontSize = 16.sp,
                        color = color.gray700
                    )
                    Text(
                        text = hour.hours,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = color.gray900
                    )
                }
            }
        }
    }
}

/**
 * 실시간 매입 시세 섹션.
 * @param prices 매입 시세 목록
 * @param lastUpdated 최종 업데이트 시간 텍스트
 */
@Composable
private fun StorePriceSection(
    prices: ImmutableList<StorePriceModel>,
    lastUpdated: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "실시간 매입 시세",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = color.gray900
            )
            Text(
                text = lastUpdated,
                fontSize = 14.sp,
                color = color.gray600
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

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
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = color.gray900
                    )
                    Text(
                        text = price.price,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = color.gray900
                    )
                }
            }
        }
    }
}

/**
 * 하단 전화 문의하기 버튼.
 * @param modifier Modifier
 * @param onClick 버튼 클릭 콜백
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
                text = "전화 문의하기",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

@Preview
@Composable
fun StoreDetailScreenPreview() {
    RebornTheme {
        StoreDetailScreen(storeId = 1).invoke(
            uiState = StoreDetailModel(
                name = "서울고물상",
                address = "서울특별시 강남구 역삼동 123-45",
                description = "정확한 계근 약속, 대량 매입 시 추가 단가 협의 가능합니다. 30년 전통의 신뢰할 수 있는 고물상입니다.",
                businessHours = DayOfWeek.entries.map {
                    BusinessHourModel(dayOfWeek = it, hours = "08:00 - 17:00")
                }.toImmutableList(),
                prices = persistentListOf(
                    StorePriceModel("고철", "450원/kg"),
                    StorePriceModel("알루미늄", "1,800원/kg"),
                    StorePriceModel("구리", "8,500원/kg"),
                    StorePriceModel("스텐", "1,150원/kg")
                ),
                lastUpdated = "최종 업데이트: 2시간 전"
            )
        )
    }
}
