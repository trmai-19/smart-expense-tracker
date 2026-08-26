package com.smartexpense.api.application.dto.response

import java.time.LocalDateTime
import java.util.UUID

data class NotificationResponseDto(
    val id: UUID,
    val type: String,
    val content: String,
    val isRead: Boolean,
    val createdAt: LocalDateTime
)
