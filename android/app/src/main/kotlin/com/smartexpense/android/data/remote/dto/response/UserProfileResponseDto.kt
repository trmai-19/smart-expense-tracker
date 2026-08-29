package com.smartexpense.android.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class UserProfileResponseDto(
    @SerializedName("id") val id: String,
    @SerializedName("email") val email: String,
    @SerializedName("displayName") val displayName: String,
    @SerializedName("avatarUrl") val avatarUrl: String?,
    @SerializedName("monthlyBudget") val monthlyBudget: Double,
    @SerializedName("streakDays") val streakDays: Int,
    @SerializedName("themeColor") val themeColor: String?
)
