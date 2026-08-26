package com.smartexpense.android.data.repository

import com.smartexpense.android.data.remote.api.ChatApi
import com.smartexpense.android.data.remote.dto.request.ChatRequestDto
import com.smartexpense.android.domain.repository.ChatRepository

class ChatRepositoryImpl(private val api: ChatApi) : ChatRepository {
    override suspend fun sendMessage(message: String): Result<String> {
        return try {
            val response = api.sendMessage(ChatRequestDto(message))
            Result.success(response.reply)
        } catch (e: Exception) {
            Result.failure(Exception("Lỗi kết nối AI: ${e.message}", e))
        }
    }
}
