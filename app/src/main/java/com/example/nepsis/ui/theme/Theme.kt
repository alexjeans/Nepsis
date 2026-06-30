package com.example.nepsis.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = NepsisGreenMain,
    onPrimary = NepsisTextPrimary,
    secondary = NepsisGreenAccent,
    onSecondary = NepsisTextPrimary,
    tertiary = NepsisGreenDark,
    background = NepsisBackground,
    onBackground = NepsisTextPrimary,
    surface = White,
    onSurface = NepsisTextPrimary
)

@Composable
fun NepsisTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
