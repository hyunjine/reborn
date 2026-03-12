package com.hyunjine.reborn.common.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import reborn.composeapp.generated.resources.Res
import reborn.composeapp.generated.resources.pretendard_bold
import reborn.composeapp.generated.resources.pretendard_medium
import reborn.composeapp.generated.resources.pretendard_regular
import reborn.composeapp.generated.resources.pretendard_semibold

/**
 * Pretendard 폰트 패밀리를 반환합니다.
 * Compose Multiplatform의 리소스 시스템을 통해 폰트를 로드합니다.
 */
@Composable
fun PretendardFontFamily(): FontFamily = FontFamily(
    Font(Res.font.pretendard_regular, FontWeight.Normal),
    Font(Res.font.pretendard_medium, FontWeight.Medium),
    Font(Res.font.pretendard_semibold, FontWeight.SemiBold),
    Font(Res.font.pretendard_bold, FontWeight.Bold),
)

@Immutable
data class AppTypography(
    val headingBold24: TextStyle,
    val headingBold18: TextStyle,
    val headingSemibold18: TextStyle,
    val titleSemibold16: TextStyle,
    val bodyRegular16: TextStyle,
    val bodyMedium16: TextStyle,
    val bodyRegular14: TextStyle,
    val bodySemibold14: TextStyle,
    val bodyMedium14: TextStyle,
    val captionRegular14: TextStyle,
    val captionMedium12: TextStyle,
)

val LocalTypography = staticCompositionLocalOf<AppTypography> {
    error("AppTypography not provided")
}

val typography: AppTypography
    @Composable
    @ReadOnlyComposable
    get() = LocalTypography.current

@Composable
fun appTypography(fontFamily: FontFamily = PretendardFontFamily()): AppTypography = AppTypography(
    headingBold24 = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
    ),
    headingBold18 = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 28.sp,
    ),
    headingSemibold18 = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 28.sp,
    ),
    titleSemibold16 = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    bodyRegular16 = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    bodyMedium16 = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    bodyRegular14 = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    bodySemibold14 = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    bodyMedium14 = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    captionRegular14 = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    captionMedium12 = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),
)
