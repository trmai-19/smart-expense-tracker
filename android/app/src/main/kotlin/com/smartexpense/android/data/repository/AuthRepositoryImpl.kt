package com.smartexpense.android.data.repository

import com.smartexpense.android.data.remote.api.AuthApi
import com.smartexpense.android.data.remote.dto.request.LoginRequestDto
import com.smartexpense.android.data.remote.dto.request.RegisterRequestDto
import com.smartexpense.android.data.remote.dto.response.AuthResponseDto
import com.smartexpense.android.domain.repository.AuthRepository

class AuthRepositoryImpl(private val api: AuthApi) : AuthRepository {
    override suspend fun login(request: LoginRequestDto): Result<AuthResponseDto> {
        return try {
            val response = api.login(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(Exception("Đăng nhập thất bại: ${e.message}", e))
        }
    }

    override suspend fun register(request: RegisterRequestDto): Result<AuthResponseDto> {
        return try {
            val response = api.register(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(Exception("Đăng ký thất bại: ${e.message}", e))
        }
    }
}
