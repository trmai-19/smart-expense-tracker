package com.smartexpense.api.presentation.controller

import com.smartexpense.api.application.dto.request.ChatRequestDto
import com.smartexpense.api.application.dto.response.ChatResponseDto
import com.smartexpense.api.application.port.`in`.ChatUseCase
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/chat")
class ChatController(
    private val chatUseCase: ChatUseCase
) {

    @PostMapping("/send")
    fun sendMessage(
        @Valid @RequestBody request: ChatRequestDto,
        authentication: Authentication
    ): ResponseEntity<ChatResponseDto> {
        val email = authentication.name
        return ResponseEntity.ok(chatUseCase.sendMessage(email, request))
    }
}
