package com.smartexpense.android.presentation.util

import android.content.Context
import androidx.compose.ui.graphics.Color
import com.smartexpense.android.ui.theme.*

object ThemeManager {
    private const val PREF_NAME = "set_theme_prefs"
    private const val KEY_ACCENT_COLOR = "selected_accent_color"

    enum class AccentColor(val composeColor: Color, val displayName: String) {
        NEON_LIME(NeonLime,   "Xanh Neon"),
        NEON_CYAN(NeonCyan,   "Xanh Cyan"),
        NEON_YELLOW(NeonYellow, "Vàng Neon"),
        NEON_PINK(NeonPink,   "Hồng Neon"),
        NEON_PURPLE(NeonPurple, "Tím Neon"),
        NEON_ORANGE(NeonOrange, "Cam Neon"),
        NEON_MINT(NeonMint,   "Xanh Mint"),
        NEON_CORAL(NeonCoral, "Đỏ Neon")
    }

    fun setAccentColor(context: Context, color: AccentColor) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_ACCENT_COLOR, color.name).apply()
    }

    fun getAccentColor(context: Context): AccentColor {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val name = prefs.getString(KEY_ACCENT_COLOR, AccentColor.NEON_YELLOW.name)
            ?: AccentColor.NEON_YELLOW.name
        return try {
            AccentColor.valueOf(name)
        } catch (e: IllegalArgumentException) {
            AccentColor.NEON_YELLOW
        }
    }

    fun getAccentComposeColor(context: Context): Color {
        return getAccentColor(context).composeColor
    }
}
