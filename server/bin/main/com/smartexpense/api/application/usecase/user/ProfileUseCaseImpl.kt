package com.smartexpense.api.application.usecase.user

import com.smartexpense.api.application.dto.request.UpdateProfileRequestDto
import com.smartexpense.api.application.dto.response.UserProfileResponseDto
import com.smartexpense.api.application.port.`in`.ProfileUseCase
import com.smartexpense.api.domain.model.User
import com.smartexpense.api.domain.repository.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ProfileUseCaseImpl(
    private val userRepository: UserRepository
) : ProfileUseCase {

    override fun getProfile(email: String): UserProfileResponseDto {
        val user = userRepository.findByEmail(email)
            ?: throw RuntimeException("User not found")
        return mapToDto(user)
    }

    override fun updateProfile(email: String, request: UpdateProfileRequestDto): UserProfileResponseDto {
        val user = userRepository.findByEmail(email)
            ?: throw RuntimeException("User not found")

        val updatedUser = user.copy(
            displayName = request.displayName ?: user.displayName,
            avatarUrl = request.avatarUrl ?: user.avatarUrl,
            monthlyBudget = request.monthlyBudget ?: user.monthlyBudget,
            themeColor = request.themeColor ?: user.themeColor,
            updatedAt = LocalDateTime.now()
        )

        return mapToDto(userRepository.save(updatedUser))
    }

    private fun mapToDto(user: User) = UserProfileResponseDto(
        id = user.id!!,
        email = user.email,
        displayName = user.displayName,
        avatarUrl = user.avatarUrl,
        monthlyBudget = user.monthlyBudget,
        streakDays = user.streakDays ?: 0,
        themeColor = user.themeColor ?: "#FFE600"
    )
}
