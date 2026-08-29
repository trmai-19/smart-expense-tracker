package com.smartexpense.android.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class AuthResponseDto(
    @SerializedName("token") val token: String,
    @SerializedName("refreshToken") val refreshToken: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("displayName") val displayName: String,
    @SerializedName("email") val email: String
)
