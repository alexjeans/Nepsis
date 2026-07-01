package com.example.nepsis.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

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

private val DarkColorScheme = darkColorScheme(
    primary = NepsisGreenMain,
    onPrimary = White,
    secondary = NepsisGreenAccent,
    onSecondary = White,
    tertiary = NepsisGreenDark,
    background = Color(0xFF121212),
    onBackground = White,
    surface = Color(0xFF1E1E1E),
    onSurface = White
)

@Composable
fun NepsisTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
