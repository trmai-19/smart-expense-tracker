package com.smartexpense.android.domain.usecase.chat

import com.smartexpense.android.domain.repository.ChatRepository
import com.smartexpense.android.presentation.chat.ChatMessage

class GetChatHistoryUseCase(private val repository: ChatRepository) {
    suspend fun execute(): Result<List<ChatMessage>> {
        return repository.getHistory()
    }
}
