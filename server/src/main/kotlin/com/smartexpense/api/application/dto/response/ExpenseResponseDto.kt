package com.smartexpense.api.application.dto.response

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

data class ExpenseResponseDto(
    val id: UUID,
    val amount: BigDecimal,
    val category: String,
    val photoUrl: String,
    val caption: String?,
    val expenseDate: LocalDateTime
)
