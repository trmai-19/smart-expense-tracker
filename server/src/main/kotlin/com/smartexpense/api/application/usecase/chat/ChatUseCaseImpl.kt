package com.smartexpense.api.application.usecase.chat

import com.smartexpense.api.application.dto.request.ChatRequestDto
import com.smartexpense.api.application.dto.response.ChatMessageDto
import com.smartexpense.api.application.dto.response.ChatResponseDto
import com.smartexpense.api.application.port.`in`.ChatUseCase
import com.smartexpense.api.application.port.out.AiChatPort
import com.smartexpense.api.domain.repository.ChatRepository
import com.smartexpense.api.domain.repository.ExpenseRepository
import com.smartexpense.api.domain.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class ChatUseCaseImpl(
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository,
    private val expenseRepository: ExpenseRepository,
    private val aiChatPort: AiChatPort
) : ChatUseCase {

    override fun sendMessage(email: String, request: ChatRequestDto): ChatResponseDto {
        val user = userRepository.findByEmail(email)
            ?: throw RuntimeException("User not found")

        // 1. Load ALL context data grouped by month
        val expenses = expenseRepository.findAllByUserIdOrderByExpenseDateDesc(user.id!!)
        val contextData = StringBuilder("Dữ liệu chi tiêu của người dùng:\n")
        if (expenses.isEmpty()) {
            contextData.append("Chưa có dữ liệu chi tiêu.")
        } else {
            val grouped = expenses.groupBy { "${it.expenseDate.year}-${String.format("%02d", it.expenseDate.monthValue)}" }
            for ((month, list) in grouped) {
                contextData.append("[$month] Tổng: ${list.sumOf { it.amount }} đ\n")
                list.forEach { 
                    contextData.append("- ${it.expenseDate.toLocalDate()}: ${it.category} (${it.amount} đ)\n")
                }
            }
        }

        // 2. Get history
        val history = chatRepository.findHistoryByUserId(user.id!!)
            .takeLast(10)
            .map { (if (it.role.equals("USER", true)) "user" else "model") to it.content }

        // 3. Save user msg and call AI
        chatRepository.saveMessage(user.id!!, "USER", request.message)
        val aiReply = aiChatPort.chat(request.message, history, contextData.toString())
        
        // 4. Check for 'Nothing' fallback
        val finalReply = if (aiReply.trim().equals("Nothing", ignoreCase = true)) {
            "Tôi là mô hình AI hỗ trợ quản lý chi tiêu của bạn. Đây không phải nội dung trong phạm vi của tôi."
        } else {
            aiReply
        }

        chatRepository.saveMessage(user.id!!, "AI", finalReply)

        return ChatResponseDto(reply = finalReply)
    }

    override fun getChatHistory(email: String): List<ChatMessageDto> {
        val user = userRepository.findByEmail(email)
            ?: throw RuntimeException("User not found")
            
        return chatRepository.findHistoryByUserId(user.id!!).map {
            ChatMessageDto(
                role = it.role,
                content = it.content,
                createdAt = it.createdAt.toString()
            )
        }
    }
}
