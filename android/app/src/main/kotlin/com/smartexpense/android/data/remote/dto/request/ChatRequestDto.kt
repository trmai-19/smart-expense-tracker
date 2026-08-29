package com.smartexpense.android.data.remote.dto.request

import com.google.gson.annotations.SerializedName

data class ChatRequestDto(
    @SerializedName("message") val message: String
)
