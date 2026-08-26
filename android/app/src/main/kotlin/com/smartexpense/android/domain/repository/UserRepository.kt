package com.smartexpense.android.domain.repository

import com.smartexpense.android.data.remote.dto.request.UpdateProfileRequestDto
import com.smartexpense.android.data.remote.dto.response.UserProfileResponseDto

interface UserRepository {
    suspend fun getMe(): Result<UserProfileResponseDto>
    suspend fun updateMe(request: UpdateProfileRequestDto): Result<UserProfileResponseDto>
}
