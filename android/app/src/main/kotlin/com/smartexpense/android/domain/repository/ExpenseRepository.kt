package com.smartexpense.android.domain.repository

import com.smartexpense.android.data.remote.dto.request.ExpenseRequestDto
import com.smartexpense.android.data.remote.dto.response.ExpenseResponseDto

interface ExpenseRepository {
    suspend fun getExpenses(): Result<List<ExpenseResponseDto>>
    suspend fun createExpense(request: ExpenseRequestDto): Result<ExpenseResponseDto>
}
