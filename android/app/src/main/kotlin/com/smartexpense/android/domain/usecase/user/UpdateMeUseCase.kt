package com.smartexpense.android.domain.usecase.user

import com.smartexpense.android.data.remote.dto.request.UpdateProfileRequestDto
import com.smartexpense.android.data.remote.dto.response.UserProfileResponseDto
import com.smartexpense.android.domain.repository.UserRepository

class UpdateMeUseCase(private val repository: UserRepository) {
    suspend fun execute(displayName: String?, avatarUrl: String?, monthlyBudget: Double?, themeColor: String?): Result<UserProfileResponseDto> {
        val request = UpdateProfileRequestDto(displayName, avatarUrl, monthlyBudget, themeColor)
        return repository.updateMe(request)
    }
}
