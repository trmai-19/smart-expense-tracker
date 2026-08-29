package com.smartexpense.api.application.dto.response

data class StatisticsResponseDto(
    val totalAmount: Double,
    val categories: List<CategoryBreakdownDto>,
    val bars: List<BarEntryDto>
)

data class CategoryBreakdownDto(
    val category: String,
    val amount: Double
)

data class BarEntryDto(
    val label: String,
    val amount: Double
)
