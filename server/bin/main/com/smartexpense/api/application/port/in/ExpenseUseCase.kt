package com.smartexpense.api.application.port.`in`

import com.smartexpense.api.application.dto.request.ExpenseRequestDto
import com.smartexpense.api.application.dto.response.ExpenseResponseDto

interface ExpenseUseCase {
    fun getExpenses(email: String): List<ExpenseResponseDto>
    fun createExpense(email: String, request: ExpenseRequestDto): ExpenseResponseDto
}
