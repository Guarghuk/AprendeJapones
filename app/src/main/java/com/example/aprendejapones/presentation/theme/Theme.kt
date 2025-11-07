package com.example.aprendejapones.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = PrimaryGreen,
    onPrimary = SurfaceWhite,
    primaryContainer = PrimaryGreenLight,
    onPrimaryContainer = PrimaryGreenDark,

    secondary = AccentOrange,
    onSecondary = SurfaceWhite,
    secondaryContainer = AccentOrangeLight,
    onSecondaryContainer = AccentOrangeDark,

    tertiary = AccentBlue,

    background = BackgroundGray,
    onBackground = TextPrimary,

    surface = SurfaceWhite,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceGray,
    onSurfaceVariant = TextSecondary,

    error = ErrorRed,
    onError = SurfaceWhite,

    outline = BorderGray,
    outlineVariant = BorderLight
)

/**
 * Tema principal de Kotodama
 * Actualmente solo soporta modo claro
 */
@Composable
fun KotodamaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme // Por ahora solo Light Theme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = com.example.aprendejapones.presentation.theme.Typography,
        content = content
    )
}