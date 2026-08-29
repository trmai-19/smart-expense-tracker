package com.smartexpense.api.application.dto.response

import java.math.BigDecimal
import java.util.UUID

data class UserProfileResponseDto(
    val id: UUID,
    val email: String,
    val displayName: String,
    val avatarUrl: String?,
    val monthlyBudget: BigDecimal?,
    val streakDays: Int,
    val themeColor: String
)
