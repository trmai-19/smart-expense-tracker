package com.smartexpense.android.domain.repository

import com.smartexpense.android.presentation.chat.ChatMessage

interface ChatRepository {
    suspend fun sendMessage(message: String): Result<String>
    suspend fun getHistory(): Result<List<ChatMessage>>
}
