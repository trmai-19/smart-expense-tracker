package com.smartexpense.api.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

data class Expense(
    val id: UUID? = null,
    val userId: UUID,
    val amount: BigDecimal,
    val category: String,
    val photoUrl: String,
    val caption: String? = null,
    val expenseDate: LocalDateTime,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)
