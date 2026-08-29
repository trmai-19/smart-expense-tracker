package com.smartexpense.api.application.port.`in`

import com.smartexpense.api.application.dto.request.LoginRequestDto
import com.smartexpense.api.application.dto.request.RegisterRequestDto
import com.smartexpense.api.application.dto.response.AuthResponseDto

import com.smartexpense.api.application.dto.request.RefreshTokenRequestDto

interface AuthUseCase {
    fun login(request: LoginRequestDto): AuthResponseDto
    fun register(request: RegisterRequestDto): AuthResponseDto
    fun refreshToken(request: RefreshTokenRequestDto): AuthResponseDto
}
