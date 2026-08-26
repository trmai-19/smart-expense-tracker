package com.smartexpense.android.domain.repository

import com.smartexpense.android.data.remote.dto.request.LoginRequestDto
import com.smartexpense.android.data.remote.dto.request.RegisterRequestDto
import com.smartexpense.android.data.remote.dto.response.AuthResponseDto

interface AuthRepository {
    suspend fun login(request: LoginRequestDto): Result<AuthResponseDto>
    suspend fun register(request: RegisterRequestDto): Result<AuthResponseDto>
}
