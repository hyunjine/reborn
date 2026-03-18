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
import androidx.compose.runtime.Stable
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
import com.hyunjine.reborn.common.component.NavigationItem
import com.hyunjine.reborn.common.theme.RebornTheme
import com.hyunjine.reborn.common.theme.color
import com.hyunjine.reborn.common.theme.typography
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import reborn.composeapp.generated.resources.Res
import reborn.composeapp.generated.resources.icon_24_arrow_right
import reborn.composeapp.generated.resources.icon_24_bell
import reborn.composeapp.generated.resources.icon_24_help
import reborn.composeapp.generated.resources.icon_24_home
import reborn.composeapp.generated.resources.icon_24_location
import reborn.composeapp.generated.resources.icon_24_profile
import reborn.composeapp.generated.resources.icon_24_setting
import reborn.composeapp.generated.resources.icon_24_store

/**
 * 내 정보 화면.
 * 업체 등록 여부에 따라 두 가지 상태의 UI를 표시합니다.
 * - 미등록: 업체 등록 유도 배너
 * - 등록 완료: 내 업체 카드
 */
@Serializable
object MyScreen : NavigationItem {

    /**
     * 내 정보 화면의 Stateful Wrapper입니다.
     * @param viewModel Koin을 통해 주입되는 ViewModel입니다.
     * @param onRegisterStore 업체 등록 화면으로 이동하는 콜백입니다.
     * @param onStoreDetail 내 업체 상세 화면으로 이동하는 콜백입니다.
     * @param onSetting 설정 화면으로 이동하는 콜백입니다.
     * @param onNotice 공지사항 화면으로 이동하는 콜백입니다.
     * @param onTerms 서비스 이용약관 화면으로 이동하는 콜백입니다.
     * @param onCustomerService 고객센터 화면으로 이동하는 콜백입니다.
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
     * 내 정보 화면의 Stateless UI 구현체입니다.
     * @param state 현재 화면의 UI 상태입니다.
     * @param onSettingClick 설정 아이콘 클릭 시 호출되는 콜백입니다.
     * @param onRegisterStoreClick 업체 등록하기 버튼 클릭 시 호출되는 콜백입니다.
     * @param onStoreCardClick 내 업체 카드 클릭 시 호출되는 콜백입니다.
     * @param onNoticeClick 공지사항 메뉴 클릭 시 호출되는 콜백입니다.
     * @param onTermsClick 서비스 이용약관 메뉴 클릭 시 호출되는 콜백입니다.
     * @param onCustomerServiceClick 고객센터 메뉴 클릭 시 호출되는 콜백입니다.
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
                    title = "공지사항",
                    onClick = onNoticeClick
                )
                MenuItem(
                    icon = { Icon(painterResource(Res.drawable.icon_24_bell), contentDescription = null, modifier = Modifier.size(20.dp), tint = color.gray900) },
                    title = "서비스 이용약관",
                    onClick = onTermsClick
                )
                MenuItem(
                    icon = { Icon(painterResource(Res.drawable.icon_24_help), contentDescription = null, modifier = Modifier.size(20.dp), tint = color.gray900) },
                    title = "고객센터",
                    onClick = onCustomerServiceClick
                )
            }
        }
    }

    override val icon: DrawableResource = Res.drawable.icon_24_profile
    override val label: String = "내 정보"

}

/**
 * 내 정보 화면의 상단 바입니다.
 * 좌측에 "내 정보" 타이틀, 우측에 설정 아이콘을 표시합니다.
 * @param onSettingClick 설정 아이콘 클릭 시 호출되는 콜백입니다.
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
            text = "내 정보",
            style = typography.headingBold24.copy(fontSize = 22.sp),
            color = color.gray900
        )
        IconButton(onClick = onSettingClick) {
            Icon(
                painter = painterResource(Res.drawable.icon_24_setting),
                contentDescription = "설정",
                modifier = Modifier.size(24.dp),
                tint = color.gray900
            )
        }
    }
}

/**
 * 사용자 프로필 카드입니다.
 * 이름과 이메일을 표시합니다.
 * @param userName 사용자 이름.
 * @param email 사용자 이메일.
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
 * 업체 미등록 시 표시되는 업체 등록 유도 배너입니다.
 * @param onRegisterClick 업체 등록하기 버튼 클릭 시 호출되는 콜백입니다.
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
                    text = "고물상을 운영하시나요?",
                    style = typography.headingBold18,
                    color = color.gray900
                )
                Text(
                    text = "지금 리본 파트너로 등록하고\n더 많은 고객을 만나보세요",
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
                text = "업체 등록하기",
                style = typography.bodySemibold14,
                color = Color.White
            )
        }
    }
}

/**
 * 업체 등록 완료 시 표시되는 내 업체 카드입니다.
 * 업체 이미지, 이름, 주소를 표시합니다.
 * @param storeInfo 업체 정보 모델.
 * @param onClick 카드 클릭 시 호출되는 콜백입니다.
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
            contentDescription = "${storeInfo.name} 대표 이미지",
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
 * 메뉴 아이템입니다.
 * 좌측에 아이콘, 중앙에 제목, 우측에 화살표를 표시합니다.
 * @param icon 아이콘 Composable.
 * @param title 메뉴 제목.
 * @param onClick 메뉴 클릭 시 호출되는 콜백입니다.
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
 * 내 정보 화면 프리뷰 (업체 미등록 상태).
 */
@Preview(showBackground = true)
@Composable
private fun MyScreenNoStorePreview() {
    RebornTheme {
        MyScreen(
            state = MyModel(
                userName = "김철수",
                email = "kimcs@example.com",
                hasStore = false,
                storeInfo = null
            )
        )
    }
}

/**
 * 내 정보 화면 프리뷰 (업체 등록 완료 상태).
 */
@Preview(showBackground = true)
@Composable
private fun MyScreenWithStorePreview() {
    RebornTheme {
        MyScreen(
            state = MyModel(
                userName = "김철수",
                email = "kimcs@example.com",
                hasStore = true,
                storeInfo = MyStoreModel(
                    name = "서울고물상",
                    address = "서울특별시 강남구 역삼동 123-45",
                    imageUrl = ""
                )
            )
        )
    }
}
