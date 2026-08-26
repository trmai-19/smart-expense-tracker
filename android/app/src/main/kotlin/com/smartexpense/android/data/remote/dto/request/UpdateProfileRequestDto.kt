package com.smartexpense.android.data.remote.dto.request

import com.google.gson.annotations.SerializedName

data class UpdateProfileRequestDto(
    @SerializedName("displayName") val displayName: String?,
    @SerializedName("avatarUrl") val avatarUrl: String?,
    @SerializedName("monthlyBudget") val monthlyBudget: Double?,
    @SerializedName("themeColor") val themeColor: String?
)
