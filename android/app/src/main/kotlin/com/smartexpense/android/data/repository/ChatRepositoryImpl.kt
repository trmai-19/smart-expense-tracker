package com.smartexpense.android.data.repository

import com.smartexpense.android.data.remote.api.ChatApi
import com.smartexpense.android.data.remote.dto.request.ChatRequestDto
import com.smartexpense.android.domain.repository.ChatRepository
import com.smartexpense.android.presentation.chat.ChatMessage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ChatRepositoryImpl(private val api: ChatApi) : ChatRepository {
    override suspend fun sendMessage(message: String): Result<String> {
        return try {
            val response = api.sendMessage(ChatRequestDto(message))
            Result.success(response.reply)
        } catch (e: Exception) {
            Result.failure(Exception("Lỗi kết nối AI: ${e.message}", e))
        }
    }

    override suspend fun getHistory(): Result<List<ChatMessage>> {
        return try {
            val response = api.getChatHistory()
            val mapped = response.map { dto ->
                // dto.createdAt is ISO string like "2026-08-28T22:30:59.402" or with Z
                val parsed = try {
                    // Cố gắng parse kiểu ZonedDateTime hoặc LocalDateTime
                    val temporal = java.time.ZonedDateTime.parse(dto.createdAt).toLocalDateTime()
                    temporal
                } catch (e: Exception) {
                    try {
                        LocalDateTime.parse(dto.createdAt)
                    } catch (e2: Exception) {
                        LocalDateTime.now()
                    }
                }
                
                ChatMessage(
                    content = dto.content,
                    isUser = dto.role.equals("USER", ignoreCase = true),
                    time = parsed.format(DateTimeFormatter.ofPattern("HH:mm"))
                )
            }
            Result.success(mapped)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
