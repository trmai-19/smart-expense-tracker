package com.smartexpense.android.presentation.util

import android.content.Context
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object UserManager {
    private const val PREF_NAME = "set_user_pref"
    private const val KEY_USER_NAME = "key_user_name"
    private const val KEY_USER_EMAIL = "key_user_email"
    private const val KEY_USER_PHONE = "key_user_phone"
    private const val KEY_AVATAR_EMOJI = "key_avatar_emoji"
    private const val KEY_MONTHLY_BUDGET = "key_monthly_budget"
    private const val KEY_STREAK_DAYS = "key_streak_days"
    private const val KEY_TOTAL_BILLS = "key_total_bills"
    private const val KEY_AUTH_TOKEN = "key_auth_token"
    private const val KEY_REFRESH_TOKEN = "key_auth_refresh_token"

    private const val DEFAULT_USER_NAME = "Nguyễn Văn A"
    private const val DEFAULT_USER_EMAIL = "user@smartexpense.app"
    private const val DEFAULT_USER_PHONE = "0912 345 678"
    private const val DEFAULT_AVATAR_EMOJI = "✨"
    private const val DEFAULT_MONTHLY_BUDGET = 5000000L

    fun saveToken(context: Context, token: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_AUTH_TOKEN, token).apply()
    }

    fun saveRefreshToken(context: Context, token: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_REFRESH_TOKEN, token).apply()
    }

    fun getToken(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_AUTH_TOKEN, null)
    }

    fun getRefreshToken(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_REFRESH_TOKEN, null)
    }

    fun clearToken(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .remove(KEY_AUTH_TOKEN)
            .remove(KEY_REFRESH_TOKEN)
            .apply()
    }

    fun getUserName(context: Context?): String {
        if (context == null) return DEFAULT_USER_NAME
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_USER_NAME, DEFAULT_USER_NAME) ?: DEFAULT_USER_NAME
    }

    fun setUserName(context: Context?, name: String?) {
        if (context == null || name.isNullOrBlank()) return
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_USER_NAME, name.trim()).apply()
    }

    fun getUserEmail(context: Context?): String {
        if (context == null) return DEFAULT_USER_EMAIL
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_USER_EMAIL, DEFAULT_USER_EMAIL) ?: DEFAULT_USER_EMAIL
    }

    fun setUserEmail(context: Context?, email: String?) {
        if (context == null || email.isNullOrBlank()) return
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_USER_EMAIL, email.trim()).apply()
    }

    fun getUserPhone(context: Context?): String {
        if (context == null) return DEFAULT_USER_PHONE
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_USER_PHONE, DEFAULT_USER_PHONE) ?: DEFAULT_USER_PHONE
    }

    fun setUserPhone(context: Context?, phone: String?) {
        if (context == null || phone.isNullOrBlank()) return
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_USER_PHONE, phone.trim()).apply()
    }

    fun getAvatarEmoji(context: Context?): String {
        if (context == null) return DEFAULT_AVATAR_EMOJI
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_AVATAR_EMOJI, DEFAULT_AVATAR_EMOJI) ?: DEFAULT_AVATAR_EMOJI
    }

    fun setAvatarEmoji(context: Context?, emoji: String?) {
        if (context == null || emoji == null) return
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_AVATAR_EMOJI, emoji.trim()).apply()
    }

    fun getMonthlyBudget(context: Context?): Long {
        if (context == null) return DEFAULT_MONTHLY_BUDGET
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getLong(KEY_MONTHLY_BUDGET, DEFAULT_MONTHLY_BUDGET)
    }

    fun setMonthlyBudget(context: Context?, budget: Long) {
        if (context == null || budget <= 0) return
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putLong(KEY_MONTHLY_BUDGET, budget).apply()
    }

    fun formatCurrency(amount: Long): String {
        val symbols = DecimalFormatSymbols(Locale("vi", "VN")).apply {
            groupingSeparator = '.'
        }
        val formatter = DecimalFormat("#,###", symbols)
        return "${formatter.format(amount)} ₫"
    }

    fun getStreakDays(context: Context?): Int {
        if (context == null) return 5
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_STREAK_DAYS, 5)
    }

    fun getTotalBillsCount(context: Context?): Int {
        if (context == null) return 24
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_TOTAL_BILLS, 24)
    }
}
