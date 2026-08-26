package com.smartexpense.android.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class NotificationResponseDto(
    @SerializedName("id") val id: String,
    @SerializedName("type") val type: String,
    @SerializedName("content") val content: String,
    @SerializedName("isRead") val isRead: Boolean,
    @SerializedName("createdAt") val createdAt: String
)
