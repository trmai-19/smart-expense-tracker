package com.smartexpense.android.data.remote.dto.request

import com.google.gson.annotations.SerializedName

data class RefreshTokenRequestDto(
    @SerializedName("refreshToken") val refreshToken: String
)
