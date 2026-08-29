package com.smartexpense.android.data.remote.dto.response

data class AnalyzeExpenseResponseDto(
    val amount: Long,
    val category: String,
    val photoUrl: String
)
