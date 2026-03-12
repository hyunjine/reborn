package com.hyunjine.reborn.common.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class AppColors(
    val white: Color = Color.White,
    val green50: Color = Color(0xFFF0FDF4),
    val green100: Color = Color(0xFFECFDF5),
    val green200: Color = Color(0xFFD0FAE5),
    val green300: Color = Color(0xFFB9F8CF),
    val green500: Color = Color(0xFF22C55E),
    val green700: Color = Color(0xFF009966),
    val gray50: Color = Color(0xFFF9FAFB),
    val gray100: Color = Color(0xFFF3F4F6),
    val gray200: Color = Color(0xFFE5E7EB),
    val gray300: Color = Color(0xFFD1D5DB),
    val gray400: Color = Color(0xFF99A1AF),
    val gray500: Color = Color(0xFF717182),
    val gray600: Color = Color(0xFF6A7282),
    val gray700: Color = Color(0xFF4A5565),
    val gray800: Color = Color(0xFF364153),
    val gray900: Color = Color(0xFF101828),
    val red500: Color = Color(0xFFFB2C36),
)

val LightAppColors = AppColors()

val LocalAppColors = staticCompositionLocalOf<AppColors> {
    error("AppColors not provided")
}

val color: AppColors
    @Composable
    @ReadOnlyComposable
    get() = LocalAppColors.current
