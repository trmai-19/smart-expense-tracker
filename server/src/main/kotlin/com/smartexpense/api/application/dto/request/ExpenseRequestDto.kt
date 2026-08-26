package com.smartexpense.api.application.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.LocalDateTime

data class ExpenseRequestDto(
    @field:NotNull(message = "Amount is required")
    val amount: BigDecimal,

    @field:NotBlank(message = "Category is required")
    val category: String,

    @field:NotBlank(message = "Photo URL is required")
    val photoUrl: String,

    val caption: String? = null,

    @field:NotNull(message = "Expense date is required")
    val expenseDate: LocalDateTime
)
