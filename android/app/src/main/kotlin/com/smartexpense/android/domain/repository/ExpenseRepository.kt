package com.smartexpense.android.domain.repository

import com.smartexpense.android.data.remote.dto.request.ExpenseRequestDto
import com.smartexpense.android.data.remote.dto.response.ExpenseResponseDto

interface ExpenseRepository {
    suspend fun getExpenses(): Result<List<ExpenseResponseDto>>
    suspend fun createExpense(request: ExpenseRequestDto): Result<ExpenseResponseDto>
    suspend fun analyzeExpense(imagePath: String, caption: String?): Result<com.smartexpense.android.data.remote.dto.response.AnalyzeExpenseResponseDto>
    suspend fun getStatistics(period: String, fromDate: String? = null, toDate: String? = null): Result<com.smartexpense.android.data.remote.dto.response.StatisticsResponseDto>
    suspend fun deleteFile(url: String): Result<Unit>
}
