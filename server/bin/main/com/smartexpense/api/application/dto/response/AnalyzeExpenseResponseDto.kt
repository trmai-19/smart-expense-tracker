package com.smartexpense.api.application.dto.response

data class AnalyzeExpenseResponseDto(
    val amount: Long,
    val category: String,
    val photoUrl: String
)
