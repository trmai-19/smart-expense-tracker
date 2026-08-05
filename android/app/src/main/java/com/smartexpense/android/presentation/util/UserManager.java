package com.smartexpense.android.presentation.util;

import android.content.Context;
import android.content.SharedPreferences;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class UserManager {
    private static final String PREF_NAME = "set_user_pref";
    private static final String KEY_USER_NAME = "key_user_name";
    private static final String KEY_USER_EMAIL = "key_user_email";
    private static final String KEY_USER_PHONE = "key_user_phone";
    private static final String KEY_AVATAR_EMOJI = "key_avatar_emoji";
    private static final String KEY_MONTHLY_BUDGET = "key_monthly_budget";
    private static final String KEY_STREAK_DAYS = "key_streak_days";
    private static final String KEY_TOTAL_BILLS = "key_total_bills";

    private static final String DEFAULT_USER_NAME = "Nguyễn Văn A";
    private static final String DEFAULT_USER_EMAIL = "user@smartexpense.app";
    private static final String DEFAULT_USER_PHONE = "0912 345 678";
    private static final String DEFAULT_AVATAR_EMOJI = "⭐";
    private static final long DEFAULT_MONTHLY_BUDGET = 5000000L;

    public static String getUserName(Context context) {
        if (context == null) return DEFAULT_USER_NAME;
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_USER_NAME, DEFAULT_USER_NAME);
    }

    public static void setUserName(Context context, String name) {
        if (context == null || name == null || name.trim().isEmpty()) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_USER_NAME, name.trim()).apply();
    }

    public static String getUserEmail(Context context) {
        if (context == null) return DEFAULT_USER_EMAIL;
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_USER_EMAIL, DEFAULT_USER_EMAIL);
    }

    public static void setUserEmail(Context context, String email) {
        if (context == null || email == null || email.trim().isEmpty()) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_USER_EMAIL, email.trim()).apply();
    }

    public static String getUserPhone(Context context) {
        if (context == null) return DEFAULT_USER_PHONE;
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_USER_PHONE, DEFAULT_USER_PHONE);
    }

    public static void setUserPhone(Context context, String phone) {
        if (context == null || phone == null || phone.trim().isEmpty()) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_USER_PHONE, phone.trim()).apply();
    }

    public static String getAvatarEmoji(Context context) {
        if (context == null) return DEFAULT_AVATAR_EMOJI;
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_AVATAR_EMOJI, DEFAULT_AVATAR_EMOJI);
    }

    public static void setAvatarEmoji(Context context, String emoji) {
        if (context == null || emoji == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_AVATAR_EMOJI, emoji.trim()).apply();
    }

    public static long getMonthlyBudget(Context context) {
        if (context == null) return DEFAULT_MONTHLY_BUDGET;
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getLong(KEY_MONTHLY_BUDGET, DEFAULT_MONTHLY_BUDGET);
    }

    public static void setMonthlyBudget(Context context, long budget) {
        if (context == null || budget <= 0) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putLong(KEY_MONTHLY_BUDGET, budget).apply();
    }

    public static String formatCurrency(long amount) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("vi", "VN"));
        symbols.setGroupingSeparator('.');
        DecimalFormat formatter = new DecimalFormat("#,###", symbols);
        return formatter.format(amount) + " ₫";
    }

    public static int getStreakDays(Context context) {
        if (context == null) return 5;
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_STREAK_DAYS, 5);
    }

    public static int getTotalBillsCount(Context context) {
        if (context == null) return 24;
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_TOTAL_BILLS, 24);
    }
}
