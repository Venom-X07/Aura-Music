package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun AuraTheme(
    themeMode: Int = 1, // 0 = Light, 1 = Dark, 2 = System
    accentColorHex: String = "#FF3B30",
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        0 -> false
        1 -> true
        else -> isSystemInDarkTheme()
    }

    val primaryColor = try {
        Color(android.graphics.Color.parseColor(accentColorHex))
    } catch (e: Exception) {
        AppleRed
    }

    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = primaryColor,
            secondary = primaryColor.copy(alpha = 0.7f),
            background = DarkBackground,
            surface = DarkSurface,
            surfaceVariant = DarkSurfaceVariant,
            onBackground = DarkOnBackground,
            onSurface = DarkOnSurface,
            onPrimary = Color.White,
            outline = DarkTextSecondary
        )
    } else {
        lightColorScheme(
            primary = primaryColor,
            secondary = primaryColor.copy(alpha = 0.7f),
            background = LightBackground,
            surface = LightSurface,
            surfaceVariant = LightSurfaceVariant,
            onBackground = LightOnBackground,
            onSurface = LightOnSurface,
            onPrimary = Color.White,
            outline = LightTextSecondary
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
