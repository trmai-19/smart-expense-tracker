package com.smartexpense.android.presentation.util;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.core.content.ContextCompat;
import com.smartexpense.android.R;

public class ThemeManager {

    private static final String PREF_NAME = "set_theme_prefs";
    private static final String KEY_ACCENT_COLOR = "selected_accent_color";

    public enum AccentColor {
        NEON_LIME(R.color.accent_neon_lime, "Xanh Neon"),
        NEON_CYAN(R.color.accent_neon_cyan, "Xanh Cyan"),
        NEON_YELLOW(R.color.accent_neon_yellow, "Vàng Neon"),
        NEON_PINK(R.color.accent_neon_pink, "Hồng Neon"),
        NEON_PURPLE(R.color.accent_neon_purple, "Tím Neon"),
        NEON_ORANGE(R.color.accent_neon_orange, "Cam Neon"),
        NEON_MINT(R.color.accent_neon_mint, "Xanh Mint"),
        NEON_CORAL(R.color.accent_neon_coral, "Đỏ Neon");

        private final int colorResId;
        private final String displayName;

        AccentColor(int colorResId, String displayName) {
            this.colorResId = colorResId;
            this.displayName = displayName;
        }

        public int getColorResId() {
            return colorResId;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public static void setAccentColor(Context context, AccentColor color) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_ACCENT_COLOR, color.name()).apply();
    }

    public static AccentColor getAccentColor(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String name = prefs.getString(KEY_ACCENT_COLOR, AccentColor.NEON_YELLOW.name());
        try {
            return AccentColor.valueOf(name);
        } catch (IllegalArgumentException e) {
            return AccentColor.NEON_YELLOW;
        }
    }

    public static int getAccentColorInt(Context context) {
        AccentColor current = getAccentColor(context);
        return ContextCompat.getColor(context, current.getColorResId());
    }
}
