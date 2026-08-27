package com.smartexpense.android.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

// Composition Local để truyền accent color động xuống toàn bộ cây Compose
val LocalAccentColor = compositionLocalOf<Color> { ThemeSunriseStart }
val LocalAccentBrush = compositionLocalOf<androidx.compose.ui.graphics.Brush> { androidx.compose.ui.graphics.Brush.linearGradient(listOf(ThemeSunriseStart, ThemeSunriseEnd)) }

val DarkColorScheme = darkColorScheme(
    primary = ThemeSunriseStart,
    background = Background,
    surface = Surface,
    onPrimary = Background,
    onBackground = OnBackground,
    onSurface = OnSurface
)

/**
 * SmartExpenseTheme
 *
 * @param accentColor Màu neon hiện tại từ ThemeManager.getAccentColor().
 * @param accentBrush Brush hiện tại.
 */
@Composable
fun SmartExpenseTheme(
    accentColor: Color = ThemeSunriseStart,
    accentBrush: androidx.compose.ui.graphics.Brush = androidx.compose.ui.graphics.Brush.linearGradient(listOf(ThemeSunriseStart, ThemeSunriseEnd)),
    content: @Composable () -> Unit
) {
    val colorScheme = darkColorScheme(
        primary          = accentColor,
        onPrimary        = Background,
        primaryContainer = accentColor.copy(alpha = 0.15f),
        onPrimaryContainer = accentColor,

        secondary        = accentColor.copy(alpha = 0.7f),
        onSecondary      = Background,

        background       = Background,
        onBackground     = OnBackground,

        surface          = Surface,
        onSurface        = OnSurface,
        surfaceVariant   = SurfaceVariant,
        onSurfaceVariant = OnSurfaceMuted,

        error            = ErrorColor,
        onError          = OnBackground,

        outline          = SurfaceCard,
        outlineVariant   = SurfaceVariant
    )

    CompositionLocalProvider(
        LocalAccentColor provides accentColor,
        LocalAccentBrush provides accentBrush
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography  = AppTypography,
            shapes      = AppShapes,
            content     = content
        )
    }
}
