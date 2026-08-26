package com.smartexpense.api.domain.model

import java.time.LocalDateTime
import java.util.UUID

data class ChatMessage(
    val id: UUID? = null,
    val userId: UUID,
    val role: String,
    val content: String,
    val createdAt: LocalDateTime? = null
)
