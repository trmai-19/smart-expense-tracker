package com.smartexpense.api.application.port.`in`

import com.smartexpense.api.application.dto.request.ExpenseRequestDto
import com.smartexpense.api.application.dto.response.ExpenseResponseDto

interface ExpenseUseCase {
    fun getExpenses(email: String): List<ExpenseResponseDto>
    fun createExpense(email: String, request: ExpenseRequestDto): ExpenseResponseDto
    fun analyzeExpense(email: String, file: org.springframework.web.multipart.MultipartFile, caption: String?): com.smartexpense.api.application.dto.response.AnalyzeExpenseResponseDto
    fun getStatistics(email: String, period: String, fromDate: String?, toDate: String?): com.smartexpense.api.application.dto.response.StatisticsResponseDto
}
