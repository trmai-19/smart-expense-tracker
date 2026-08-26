package com.smartexpense.api.application.usecase.chat

import com.smartexpense.api.application.dto.request.ChatRequestDto
import com.smartexpense.api.application.dto.response.ChatResponseDto
import com.smartexpense.api.application.port.`in`.ChatUseCase
import com.smartexpense.api.application.port.out.AiChatPort
import com.smartexpense.api.domain.repository.ChatRepository
import com.smartexpense.api.domain.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class ChatUseCaseImpl(
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository,
    private val aiChatPort: AiChatPort
) : ChatUseCase {

    override fun sendMessage(email: String, request: ChatRequestDto): ChatResponseDto {
        val user = userRepository.findByEmail(email)
            ?: throw RuntimeException("User not found")

        // Lấy lịch sử hội thoại gần nhất để cung cấp context cho AI
        val history = chatRepository.findHistoryByUserId(user.id!!)
            .takeLast(10) // Giới hạn 5 lượt (10 messages) để tránh vượt token limit
            .map { it.role.lowercase() to it.content }

        // Lưu tin nhắn người dùng
        chatRepository.saveMessage(user.id!!, "USER", request.message)

        // Gọi AI
        val aiReply = aiChatPort.chat(request.message, history)

        // Lưu phản hồi của AI
        chatRepository.saveMessage(user.id!!, "AI", aiReply)

        return ChatResponseDto(reply = aiReply)
    }
}
