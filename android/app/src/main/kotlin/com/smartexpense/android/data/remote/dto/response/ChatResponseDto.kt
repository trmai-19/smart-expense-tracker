package com.smartexpense.android.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class ChatResponseDto(
    @SerializedName("reply") val reply: String
)
