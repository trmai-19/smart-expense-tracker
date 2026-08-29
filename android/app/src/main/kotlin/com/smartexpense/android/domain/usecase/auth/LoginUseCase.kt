package com.smartexpense.android.domain.usecase.auth

import com.smartexpense.android.data.remote.dto.request.LoginRequestDto
import com.smartexpense.android.data.remote.dto.response.AuthResponseDto
import com.smartexpense.android.domain.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend fun execute(email: String, password: String): Result<AuthResponseDto> {
        val request = LoginRequestDto(email, password)
        return repository.login(request)
    }
}
