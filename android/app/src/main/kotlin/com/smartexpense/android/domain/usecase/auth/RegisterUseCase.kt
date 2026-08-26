package com.smartexpense.android.domain.usecase.auth

import com.smartexpense.android.data.remote.dto.request.RegisterRequestDto
import com.smartexpense.android.data.remote.dto.response.AuthResponseDto
import com.smartexpense.android.domain.repository.AuthRepository

class RegisterUseCase(private val repository: AuthRepository) {
    suspend fun execute(name: String, email: String, password: String): Result<AuthResponseDto> {
        val request = RegisterRequestDto(name, email, password)
        return repository.register(request)
    }
}
