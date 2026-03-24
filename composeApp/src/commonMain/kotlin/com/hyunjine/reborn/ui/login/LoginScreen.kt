package com.hyunjine.reborn.ui.login

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import com.hyunjine.reborn.common.component.RequestLocationPermission
import com.hyunjine.reborn.common.theme.RebornTheme
import com.hyunjine.reborn.common.theme.color
import com.hyunjine.reborn.common.theme.typography
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import reborn.composeapp.generated.resources.Res
import reborn.composeapp.generated.resources.icon_24_kakao
import reborn.composeapp.generated.resources.icon_24_naver
import reborn.composeapp.generated.resources.icon_logo

/**
 * 로그인 화면.
 * 카카오, 네이버 소셜 로그인을 제공합니다.
 */
@Serializable
object LoginScreen : NavKey {

    internal val KakaoYellow = Color(0xFFFEE500)
    internal val NaverGreen = Color(0xFF03C75A)

    /**
     * 로그인 화면에서 발생하는 UI 이벤트들입니다.
     */
    sealed interface UiEvent {
        /**
         * 카카오 로그인 버튼 클릭 시 발생하는 이벤트입니다.
         */
        data object KakaoLoginClicked : UiEvent

        /**
         * 네이버 로그인 버튼 클릭 시 발생하는 이벤트입니다.
         */
        data object NaverLoginClicked : UiEvent

        /**
         * 이용약관 클릭 시 발생하는 이벤트입니다.
         */
        data object TermsOfServiceClicked : UiEvent

        /**
         * 개인정보처리방침 클릭 시 발생하는 이벤트입니다.
         */
        data object PrivacyPolicyClicked : UiEvent
    }

    /**
     * 로그인 화면의 Stateful Wrapper입니다.
     * @param onLoginSuccess 로그인 성공 시 호출되는 콜백입니다.
     */
    @Composable
    operator fun invoke(
        kakaoLogin: () ->Unit,
        naverLogin: () ->Unit,
    ) {
        RequestLocationPermission(onResult = {})
        invoke(
            onEvent = { event ->
                when (event) {
                    is UiEvent.KakaoLoginClicked -> { kakaoLogin() }
                    is UiEvent.NaverLoginClicked -> { naverLogin() }
                    is UiEvent.TermsOfServiceClicked -> { /* TODO: 이용약관 페이지 이동 */ }
                    is UiEvent.PrivacyPolicyClicked -> { /* TODO: 개인정보처리방침 페이지 이동 */ }
                }
            }
        )
    }

    /**
     * 로그인 화면의 Stateless UI 구현체입니다.
     * @param onEvent UI 이벤트 처리를 위한 콜백입니다.
     */
    @Composable
    operator fun invoke(
        onEvent: (UiEvent) -> Unit = {}
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color.white)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(3f))
            LogoSection()
            Spacer(modifier = Modifier.weight(2.5f))
            LoginButtonSection(
                onKakaoLogin = { onEvent(UiEvent.KakaoLoginClicked) },
                onNaverLogin = { onEvent(UiEvent.NaverLoginClicked) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.weight(2.5f))
            TermsText(
                onTermsClick = { onEvent(UiEvent.TermsOfServiceClicked) },
                onPrivacyClick = { onEvent(UiEvent.PrivacyPolicyClicked) }
            )
            Spacer(modifier = Modifier.weight(0.6f))
        }
    }
}

/**
 * 로고 영역입니다.
 * 앱 로고 이미지, 앱 이름, 앱 설명 텍스트를 표시합니다.
 * @param modifier Modifier.
 */
@Composable
private fun LogoSection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(Res.drawable.icon_logo),
            contentDescription = "리본 로고",
            modifier = Modifier.size(108.dp)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "리본(Re-born)",
                style = typography.headingBold24,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Text(
                text = "투명한 폐자원 거래 플랫폼",
                style = typography.headingMedium18,
                color = color.gray700,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * 소셜 로그인 버튼 영역입니다.
 * 카카오, 네이버 로그인 버튼을 제공합니다.
 * @param onKakaoLogin 카카오 로그인 버튼 클릭 시 호출되는 콜백입니다.
 * @param onNaverLogin 네이버 로그인 버튼 클릭 시 호출되는 콜백입니다.
 * @param modifier Modifier.
 */
@Composable
private fun LoginButtonSection(
    onKakaoLogin: () -> Unit,
    onNaverLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SocialLoginButton(
            text = "카카오로 시작하기",
            backgroundColor = LoginScreen.KakaoYellow,
            textColor = color.gray900,
            icon = {
                Icon(
                    painter = painterResource(Res.drawable.icon_24_kakao),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = color.gray900
                )
            },
            onClick = onKakaoLogin
        )
        SocialLoginButton(
            text = "네이버로 시작하기",
            backgroundColor = LoginScreen.NaverGreen,
            textColor = color.white,
            icon = {
                Icon(
                    painter = painterResource(Res.drawable.icon_24_naver),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = color.white
                )
            },
            onClick = onNaverLogin
        )
    }
}

/**
 * 소셜 로그인 버튼입니다.
 * @param text 버튼 텍스트.
 * @param backgroundColor 버튼 배경색.
 * @param textColor 버튼 텍스트 색상.
 * @param icon 좌측 아이콘 Composable.
 * @param onClick 버튼 클릭 시 호출되는 콜백입니다.
 * @param modifier Modifier.
 */
@Composable
private fun SocialLoginButton(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon()
            Text(
                text = text,
                style = typography.titleSemibold16,
                color = textColor
            )
        }
    }
}

/**
 * 하단 이용약관 및 개인정보처리방침 안내 텍스트입니다.
 * @param onTermsClick 이용약관 클릭 시 호출되는 콜백입니다.
 * @param onPrivacyClick 개인정보처리방침 클릭 시 호출되는 콜백입니다.
 * @param modifier Modifier.
 */
@Composable
private fun TermsText(
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val termsTag = "TERMS"
    val privacyTag = "PRIVACY"

    val annotatedString = buildAnnotatedString {
        withStyle(SpanStyle(color = Color.Black)) {
            append("로그인 시 ")
        }
        pushStringAnnotation(tag = termsTag, annotation = termsTag)
        withStyle(
            SpanStyle(
                color = color.green500,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline
            )
        ) {
            append("이용약관")
        }
        pop()
        withStyle(SpanStyle(color = Color.Black)) {
            append(" 및 ")
        }
        pushStringAnnotation(tag = privacyTag, annotation = privacyTag)
        withStyle(
            SpanStyle(
                color = color.green500,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline
            )
        ) {
            append("개인정보처리방침")
        }
        pop()
        withStyle(SpanStyle(color = Color.Black)) {
            append("에 동의하게 됩니다.")
        }
    }

    Text(
        text = annotatedString,
        style = TextStyle(
            fontSize = 12.sp,
            lineHeight = 19.5.sp
        ),
        textAlign = TextAlign.Center,
        modifier = modifier.fillMaxWidth()
    )
}

/**
 * 로고 영역 프리뷰입니다.
 */
@Preview(showBackground = true)
@Composable
private fun LogoSectionPreview() {
    RebornTheme {
        LogoSection()
    }
}

/**
 * 로그인 버튼 영역 프리뷰입니다.
 */
@Preview(showBackground = true)
@Composable
private fun LoginButtonSectionPreview() {
    RebornTheme {
        LoginButtonSection(
            onKakaoLogin = {},
            onNaverLogin = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

/**
 * 로그인 화면 전체 프리뷰입니다.
 */
@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    RebornTheme {
        LoginScreen(
            onEvent = {}
        )
    }
}

/**
 * 작은 화면 디바이스에서의 로그인 화면 프리뷰입니다.
 */
@Preview(showBackground = true, widthDp = 320, heightDp = 480)
@Composable
private fun LoginScreenSmallDevicePreview() {
    RebornTheme {
        LoginScreen(
            onEvent = {}
        )
    }
}
