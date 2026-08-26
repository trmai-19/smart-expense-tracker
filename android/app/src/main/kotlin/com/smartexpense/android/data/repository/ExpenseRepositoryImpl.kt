package com.smartexpense.android.data.repository

import com.smartexpense.android.data.remote.api.ExpenseApi
import com.smartexpense.android.data.remote.dto.request.ExpenseRequestDto
import com.smartexpense.android.data.remote.dto.response.ExpenseResponseDto
import com.smartexpense.android.domain.repository.ExpenseRepository

class ExpenseRepositoryImpl(private val api: ExpenseApi) : ExpenseRepository {
    override suspend fun getExpenses(): Result<List<ExpenseResponseDto>> {
        return try {
            val response = api.getExpenses()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(Exception("Lỗi tải lịch sử chi tiêu: ${e.message}", e))
        }
    }

    override suspend fun createExpense(request: ExpenseRequestDto): Result<ExpenseResponseDto> {
        return try {
            val response = api.createExpense(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(Exception("Lỗi tạo chi tiêu: ${e.message}", e))
        }
    }
}
