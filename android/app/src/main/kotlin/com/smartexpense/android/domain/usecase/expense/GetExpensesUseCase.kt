package com.smartexpense.android.domain.usecase.expense

import com.smartexpense.android.data.remote.dto.response.ExpenseResponseDto
import com.smartexpense.android.domain.repository.ExpenseRepository

class GetExpensesUseCase(private val repository: ExpenseRepository) {
    suspend fun execute(): Result<List<ExpenseResponseDto>> {
        return repository.getExpenses()
    }
}
