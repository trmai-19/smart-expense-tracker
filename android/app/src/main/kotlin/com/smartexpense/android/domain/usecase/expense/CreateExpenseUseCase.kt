package com.smartexpense.android.domain.usecase.expense

import com.smartexpense.android.data.remote.dto.request.ExpenseRequestDto
import com.smartexpense.android.data.remote.dto.response.ExpenseResponseDto
import com.smartexpense.android.domain.repository.ExpenseRepository

class CreateExpenseUseCase(private val repository: ExpenseRepository) {
    suspend fun execute(amount: Double, category: String, photoUrl: String, caption: String, expenseDate: String): Result<ExpenseResponseDto> {
        val request = ExpenseRequestDto(amount, category, photoUrl, caption, expenseDate)
        return repository.createExpense(request)
    }
}
