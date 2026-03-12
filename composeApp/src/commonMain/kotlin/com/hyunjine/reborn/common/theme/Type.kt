package com.hyunjine.reborn.common.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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
