package com.smartexpense.android.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class AuthResponseDto(
    @SerializedName("token") val token: String,
    @SerializedName("userProfile") val userProfile: UserProfileResponseDto
)
