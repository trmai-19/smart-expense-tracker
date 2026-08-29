package com.smartexpense.api.application.dto.response

data class ChatMessageDto(
    val role: String,
    val content: String,
    val createdAt: String
)
