package com.smartexpense.android.domain.usecase.expense

import com.smartexpense.android.data.remote.dto.response.AnalyzeExpenseResponseDto
import com.smartexpense.android.domain.repository.ExpenseRepository

class AnalyzeExpenseUseCase(private val repository: ExpenseRepository) {
    suspend fun execute(imagePath: String, caption: String?): Result<AnalyzeExpenseResponseDto> {
        return repository.analyzeExpense(imagePath, caption)
    }
}
