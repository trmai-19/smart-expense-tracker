package com.smartexpense.android.presentation.util

import android.content.Context
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.smartexpense.android.ui.theme.*

object ThemeManager {
    private const val PREF_NAME = "set_theme_prefs"
    private const val KEY_ACCENT_COLOR = "selected_accent_color"

    enum class AccentColor(val composeColor: Color, val displayName: String, val startColor: Color, val endColor: Color) {
        SUNRISE(ThemeSunriseStart, "Bình Minh", ThemeSunriseStart, ThemeSunriseEnd),
        SUNSET(ThemeSunsetStart, "Hoàng Hôn", ThemeSunsetStart, ThemeSunsetEnd),
        BERRY(ThemeBerryStart, "Mâm Xôi", ThemeBerryStart, ThemeBerryEnd),
        OCEAN(ThemeOceanStart, "Đại Dương", ThemeOceanStart, ThemeOceanEnd),
        FOREST(ThemeForestStart, "Rừng Xanh", ThemeForestStart, ThemeForestEnd),
        LIME(ThemeLimeStart, "Chanh Tươi", ThemeLimeStart, ThemeLimeEnd),
        FLAMINGO(ThemeFlamingoStart, "Hồng Hạc", ThemeFlamingoStart, ThemeFlamingoEnd),
        AMETHYST(ThemeAmethystStart, "Thạch Anh", ThemeAmethystStart, ThemeAmethystEnd),
        FIRE(ThemeFireStart, "Lửa Đỏ", ThemeFireStart, ThemeFireEnd),
        TROPICAL(ThemeTropicalStart, "Nhiệt Đới", ThemeTropicalStart, ThemeTropicalEnd),
        AURORA(ThemeAuroraStart, "Cực Quang", ThemeAuroraStart, ThemeAuroraEnd),
        NEON_NIGHT(ThemeNeonNightStart, "Đêm Neon", ThemeNeonNightStart, ThemeNeonNightEnd);

        val composeBrush: Brush
            get() = Brush.linearGradient(listOf(startColor, endColor))
    }

    fun setAccentColor(context: Context, color: AccentColor) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_ACCENT_COLOR, color.name).apply()
    }

    fun getAccentColor(context: Context): AccentColor {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val name = prefs.getString(KEY_ACCENT_COLOR, AccentColor.SUNRISE.name)
            ?: AccentColor.SUNRISE.name
        return try {
            AccentColor.valueOf(name)
        } catch (e: IllegalArgumentException) {
            AccentColor.SUNRISE
        }
    }

    fun getAccentComposeColor(context: Context): Color {
        return getAccentColor(context).composeColor
    }
}
