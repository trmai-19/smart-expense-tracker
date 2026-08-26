package com.smartexpense.android.domain.usecase.chat

import com.smartexpense.android.domain.repository.ChatRepository

class SendChatMessageUseCase(private val repository: ChatRepository) {
    suspend fun execute(message: String): Result<String> {
        return repository.sendMessage(message)
    }
}
