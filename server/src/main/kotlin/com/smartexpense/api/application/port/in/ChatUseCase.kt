package com.smartexpense.api.application.port.`in`

import com.smartexpense.api.application.dto.request.ChatRequestDto
import com.smartexpense.api.application.dto.response.ChatResponseDto

interface ChatUseCase {
    fun sendMessage(email: String, request: ChatRequestDto): ChatResponseDto
}
