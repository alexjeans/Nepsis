package com.example.nepsis.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = UamPrimary,
    onPrimary = White,
    secondary = UamSuccess,
    onSecondary = White,
    tertiary = UamWarning,
    background = UamBackground,
    onBackground = UamTextPrimary,
    surface = UamSurface,
    onSurface = UamTextPrimary
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