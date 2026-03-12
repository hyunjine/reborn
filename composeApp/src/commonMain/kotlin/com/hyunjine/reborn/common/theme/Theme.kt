package com.hyunjine.reborn.common.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun RebornTheme(
    colors: AppColors = LightAppColors,
    typography: AppTypography = appTypography(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalAppColors provides colors,
        LocalTypography provides typography,
    ) {
        MaterialTheme(
            content = content
        )
    }
}
