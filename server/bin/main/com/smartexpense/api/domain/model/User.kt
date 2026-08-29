package com.smartexpense.api.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

data class User(
    val id: UUID? = null,
    val email: String,
    val passwordHash: String,
    val displayName: String,
    val avatarUrl: String? = null,
    val monthlyBudget: BigDecimal? = null,
    val streakDays: Int? = 0,
    val themeColor: String? = "#FFE600",
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)
