package com.smartexpense.android.domain.usecase.user

import com.smartexpense.android.data.remote.dto.response.UserProfileResponseDto
import com.smartexpense.android.domain.repository.UserRepository

class GetMeUseCase(private val repository: UserRepository) {
    suspend fun execute(): Result<UserProfileResponseDto> {
        return repository.getMe()
    }
}
