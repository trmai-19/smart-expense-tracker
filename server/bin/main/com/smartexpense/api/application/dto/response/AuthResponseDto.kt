package com.smartexpense.api.application.dto.response

import java.util.UUID

data class AuthResponseDto(
    val token: String,
    val refreshToken: String,
    val userId: UUID,
    val displayName: String,
    val email: String
)
