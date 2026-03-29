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
    val gray50: Color = Color(0xFFFAFAFA),
    val gray100: Color = Color(0xFFF5F5F5),
    val gray200: Color = Color(0xFFE5E5E5),
    val gray300: Color = Color(0xFFD4D4D4),
    val gray400: Color = Color(0xFFA3A3A3),
    val gray500: Color = Color(0xFF737373),
    val gray600: Color = Color(0xFF525252),
    val gray700: Color = Color(0xFF404040),
    val gray800: Color = Color(0xFF262626),
    val gray900: Color = Color(0xFF171717),
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
