package com.smartexpense.api.application.dto.request

import jakarta.validation.constraints.NotBlank

data class ChatRequestDto(
    @field:NotBlank(message = "Message cannot be blank")
    val message: String
)
