package com.smartexpense.api.domain.model

import java.time.LocalDateTime
import java.util.UUID

data class Notification(
    val id: UUID? = null,
    val userId: UUID,
    val type: String,
    val content: String,
    val isRead: Boolean = false,
    val createdAt: LocalDateTime? = null
)
