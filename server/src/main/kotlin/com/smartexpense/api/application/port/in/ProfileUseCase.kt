package com.smartexpense.api.application.port.`in`

import com.smartexpense.api.application.dto.request.UpdateProfileRequestDto
import com.smartexpense.api.application.dto.response.UserProfileResponseDto

interface ProfileUseCase {
    fun getProfile(email: String): UserProfileResponseDto
    fun updateProfile(email: String, request: UpdateProfileRequestDto): UserProfileResponseDto
}
