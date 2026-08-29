package com.smartexpense.android.domain.usecase.expense

import com.smartexpense.android.data.remote.dto.response.StatisticsResponseDto
import com.smartexpense.android.domain.repository.ExpenseRepository

class GetStatisticsUseCase(private val repository: ExpenseRepository) {
    suspend fun execute(period: String, fromDate: String? = null, toDate: String? = null): Result<StatisticsResponseDto> {
        return repository.getStatistics(period, fromDate, toDate)
    }
}
