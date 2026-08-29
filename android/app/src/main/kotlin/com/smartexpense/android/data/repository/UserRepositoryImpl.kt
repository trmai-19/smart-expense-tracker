package com.smartexpense.android.data.repository

import com.smartexpense.android.data.remote.api.UserApi
import com.smartexpense.android.data.remote.dto.request.UpdateProfileRequestDto
import com.smartexpense.android.data.remote.dto.response.UserProfileResponseDto
import com.smartexpense.android.domain.repository.UserRepository

class UserRepositoryImpl(private val api: UserApi) : UserRepository {
    override suspend fun getMe(): Result<UserProfileResponseDto> {
        return try {
            val response = api.getMe()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(Exception("Lỗi tải thông tin: ${e.message}", e))
        }
    }

    override suspend fun updateMe(request: UpdateProfileRequestDto): Result<UserProfileResponseDto> {
        return try {
            val response = api.updateMe(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(Exception("Lỗi cập nhật thông tin: ${e.message}", e))
        }
    }
}
