package com.smartexpense.api.domain.repository

interface ChatRepository {
    fun saveMessage(userId: java.util.UUID, role: String, content: String): com.smartexpense.api.domain.model.ChatMessage
    fun findHistoryByUserId(userId: java.util.UUID): List<com.smartexpense.api.domain.model.ChatMessage>
}
