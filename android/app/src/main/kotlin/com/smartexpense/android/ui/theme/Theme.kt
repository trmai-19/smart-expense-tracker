package com.smartexpense.android.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

// Composition Local để truyền accent color động xuống toàn bộ cây Compose
val LocalAccentColor = compositionLocalOf<Color> { NeonYellow }

/**
 * SmartExpenseTheme
 *
 * @param accentColor Màu neon hiện tại từ ThemeManager.getAccentColor().
 *                    Truyền vào từ Activity/Screen level sau khi đọc SharedPreferences.
 */
@Composable
fun SmartExpenseTheme(
    accentColor: Color = NeonYellow,
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

    CompositionLocalProvider(LocalAccentColor provides accentColor) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography  = AppTypography,
            shapes      = AppShapes,
            content     = content
        )
    }
}
