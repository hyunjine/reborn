package com.hyunjine.reborn.ui.my

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
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import com.hyunjine.reborn.ui.home.HomeBottomNavigation
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import reborn.composeapp.generated.resources.Res
import reborn.composeapp.generated.resources.ic_bell
import reborn.composeapp.generated.resources.ic_chart
import reborn.composeapp.generated.resources.ic_check
import reborn.composeapp.generated.resources.ic_chevron_right
import reborn.composeapp.generated.resources.ic_document
import reborn.composeapp.generated.resources.ic_dollar
import reborn.composeapp.generated.resources.ic_edit
import reborn.composeapp.generated.resources.ic_location_pin
import reborn.composeapp.generated.resources.ic_logout
import reborn.composeapp.generated.resources.ic_question
import reborn.composeapp.generated.resources.ic_store
import reborn.composeapp.generated.resources.ic_user_profile

/**
 * 내 정보 화면.
 * 업체 등록 여부에 따라 두 가지 상태의 UI를 표시합니다.
 */
@Serializable
object MyScreen : NavKey {

    /**
     * 내 정보 화면의 UI 이벤트.
     */
    sealed interface UiEvent {
        /** 업체 등록하기 버튼 클릭 */
        data object RegisterStoreClicked : UiEvent

        /** 영업 상태 토글 */
        data class ToggleStoreOpen(val isOpen: Boolean) : UiEvent

        /** 정보 수정 클릭 */
        data object EditInfoClicked : UiEvent

        /** 단가 관리 클릭 */
        data object PriceManageClicked : UiEvent

        /** 통계 클릭 */
        data object StatsClicked : UiEvent

        /** 공지사항 클릭 */
        data object NoticeClicked : UiEvent

        /** 고객센터 클릭 */
        data object SupportClicked : UiEvent

        /** 서비스 이용약관 클릭 */
        data object TermsClicked : UiEvent

        /** 로그아웃 클릭 */
        data object LogoutClicked : UiEvent

        /** 하단 네비게이션 클릭 */
        data class NavClicked(val route: String) : UiEvent
    }

    /**
     * Stateful Wrapper. Koin ViewModel을 주입받고 이벤트를 처리합니다.
     * @param viewModel Koin에서 주입받는 ViewModel
     * @param onRegisterStore 업체 등록 화면 이동 콜백
     * @param onNavClick 네비게이션 아이템 클릭 콜백
     */
    @Composable
    operator fun invoke(
        viewModel: MyViewModel = koinViewModel(),
        onRegisterStore: () -> Unit = {},
        onNavClick: (String) -> Unit = {}
    ) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        invoke(
            uiState = uiState,
            onEvent = { event ->
                when (event) {
                    is UiEvent.RegisterStoreClicked -> onRegisterStore()
                    is UiEvent.NavClicked -> onNavClick(event.route)
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
        uiState: MyModel,
        onEvent: (UiEvent) -> Unit = {}
    ) {
        Scaffold(
            bottomBar = {
                HomeBottomNavigation(
                    selectedRoute = "my",
                    onNavClick = { onEvent(UiEvent.NavClicked(it)) }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .verticalScroll(rememberScrollState())
                    .padding(padding)
            ) {
                HeaderSection()
                ProfileSection(userName = uiState.userName, email = uiState.email)

                if (uiState.hasStore && uiState.storeInfo != null) {
                    MyStoreSection(
                        storeInfo = uiState.storeInfo,
                        onToggleOpen = { onEvent(UiEvent.ToggleStoreOpen(it)) }
                    )
                    StoreManagementSection(onEvent = onEvent)
                } else {
                    RegisterStoreCta(onClick = { onEvent(UiEvent.RegisterStoreClicked) })
                }

                MenuSection(onEvent = onEvent)
            }
        }
    }
}

/**
 * 헤더 섹션. "내 정보" 타이틀을 표시합니다.
 */
@Composable
private fun HeaderSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Text(
            text = "내 정보",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF101828)
        )
    }
}

/**
 * 프로필 섹션. 아바타, 이름, 이메일을 표시합니다.
 * @param userName 사용자 이름
 * @param email 사용자 이메일
 */
@Composable
private fun ProfileSection(userName: String, email: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(Color(0xFFD0FAE5), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_user_profile),
                contentDescription = "프로필",
                modifier = Modifier.size(40.dp),
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = userName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF101828)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = email,
                fontSize = 14.sp,
                color = Color(0xFF6A7282)
            )
        }
    }
}

/**
 * 업체 등록 유도 CTA 카드. 업체가 없을 때 표시합니다.
 * @param onClick 업체 등록하기 버튼 클릭 콜백
 */
@Composable
private fun RegisterStoreCta(onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 18.dp, bottom = 18.dp)
            .background(Color(0xFFF9FAFB), RoundedCornerShape(16.dp))
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            // Store icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFFD0FAE5), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_store),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = Color(0xFF22C55E)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = "고물상을 운영하시나요?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF101828)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "지금 리본 파트너로 등록하고\n더 많은 고객을 만나보세요!",
                    fontSize = 14.sp,
                    color = Color(0xFF6A7282),
                    lineHeight = 22.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF22C55E)
            )
        ) {
            Text(
                text = "업체 등록하기",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

/**
 * 내 업체 섹션. 업체가 있을 때 업체 카드를 표시합니다.
 * @param storeInfo 업체 정보
 * @param onToggleOpen 영업 상태 토글 콜백
 */
@Composable
private fun MyStoreSection(
    storeInfo: MyStoreModel,
    onToggleOpen: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // Section title
        Text(
            text = "내 업체",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF101828),
            modifier = Modifier.padding(vertical = 20.dp)
        )

        // Store card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
        ) {
            // Store image placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(195.dp)
                    .background(Color(0xFFF3F4F6))
            ) {
                // Verified badge
                if (storeInfo.isVerified) {
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .background(Color.White, RoundedCornerShape(16.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_check),
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = Color(0xFF22C55E)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "인증 업체",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF101828)
                        )
                    }
                }
            }

            // Store info
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF9FAFB))
                    .padding(16.dp)
            ) {
                Text(
                    text = storeInfo.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF101828)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Address
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_location_pin),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color(0xFF99A1AF)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = storeInfo.address,
                        fontSize = 14.sp,
                        color = Color(0xFF6A7282)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Business status toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "영업 상태",
                        fontSize = 14.sp,
                        color = Color(0xFF4A5565)
                    )
                    Switch(
                        checked = storeInfo.isOpen,
                        onCheckedChange = onToggleOpen,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF22C55E),
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color(0xFFD1D5DB)
                        )
                    )
                }

                // Status text
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = if (storeInfo.isOpen) "영업 중" else "영업 종료",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (storeInfo.isOpen) Color(0xFF22C55E) else Color(0xFF99A1AF)
                    )
                }
            }
        }
    }
}

/**
 * 업체 관리 메뉴 섹션. 정보 수정, 단가 관리, 통계 메뉴를 표시합니다.
 * @param onEvent UI 이벤트 콜백
 */
@Composable
private fun StoreManagementSection(onEvent: (MyScreen.UiEvent) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ) {
        Text(
            text = "업체 관리",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF6A7282),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )
        MenuItem(
            icon = painterResource(Res.drawable.ic_edit),
            label = "정보 수정",
            onClick = { onEvent(MyScreen.UiEvent.EditInfoClicked) }
        )
        MenuItem(
            icon = painterResource(Res.drawable.ic_dollar),
            label = "단가 관리",
            onClick = { onEvent(MyScreen.UiEvent.PriceManageClicked) }
        )
        MenuItemWithBadge(
            icon = painterResource(Res.drawable.ic_chart),
            label = "통계",
            badge = "추후",
            onClick = { onEvent(MyScreen.UiEvent.StatsClicked) }
        )
    }
}

/**
 * 일반 메뉴 섹션. 공지사항, 고객센터, 서비스 이용약관, 로그아웃을 표시합니다.
 * @param onEvent UI 이벤트 콜백
 */
@Composable
private fun MenuSection(onEvent: (MyScreen.UiEvent) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ) {
        MenuItem(
            icon = painterResource(Res.drawable.ic_bell),
            label = "공지사항",
            onClick = { onEvent(MyScreen.UiEvent.NoticeClicked) }
        )
        MenuItem(
            icon = painterResource(Res.drawable.ic_question),
            label = "고객센터",
            onClick = { onEvent(MyScreen.UiEvent.SupportClicked) }
        )
        MenuItem(
            icon = painterResource(Res.drawable.ic_document),
            label = "서비스 이용약관",
            onClick = { onEvent(MyScreen.UiEvent.TermsClicked) }
        )
        MenuItem(
            icon = painterResource(Res.drawable.ic_logout),
            label = "로그아웃",
            onClick = { onEvent(MyScreen.UiEvent.LogoutClicked) }
        )
    }
}

/**
 * 메뉴 아이템. 아이콘, 라벨, 화살표를 표시합니다.
 * @param icon 메뉴 아이콘
 * @param label 메뉴 라벨
 * @param onClick 클릭 콜백
 */
@Composable
private fun MenuItem(
    icon: Painter,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = Color(0xFF4A5565)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            fontSize = 16.sp,
            color = Color(0xFF101828),
            modifier = Modifier.weight(1f)
        )
        Icon(
            painter = painterResource(Res.drawable.ic_chevron_right),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = Color(0xFF99A1AF)
        )
    }
}

/**
 * 배지가 있는 메뉴 아이템.
 * @param icon 메뉴 아이콘
 * @param label 메뉴 라벨
 * @param badge 배지 텍스트
 * @param onClick 클릭 콜백
 */
@Composable
private fun MenuItemWithBadge(
    icon: Painter,
    label: String,
    badge: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = Color(0xFF4A5565)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            fontSize = 16.sp,
            color = Color(0xFF101828)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = badge,
            fontSize = 11.sp,
            color = Color(0xFF99A1AF),
            modifier = Modifier
                .background(Color(0xFFF3F4F6), RoundedCornerShape(4.dp))
                .padding(horizontal = 8.dp, vertical = 2.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            painter = painterResource(Res.drawable.ic_chevron_right),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = Color(0xFF99A1AF)
        )
    }
}

@Preview
@Composable
fun MyScreenNoStorePreview() {
    MyScreen(
        uiState = MyModel(
            userName = "김철수",
            email = "kimcs@example.com",
            hasStore = false
        )
    )
}

@Preview
@Composable
fun MyScreenWithStorePreview() {
    MyScreen(
        uiState = MyModel(
            userName = "김철수",
            email = "kimcs@example.com",
            hasStore = true,
            storeInfo = MyStoreModel(
                name = "서울고물상",
                address = "서울특별시 강남구 역삼동 123-45",
                isVerified = true,
                isOpen = true
            )
        )
    )
}
