package com.smartexpense.android.data.repository

import com.smartexpense.android.data.remote.api.ExpenseApi
import com.smartexpense.android.data.remote.dto.request.ExpenseRequestDto
import com.smartexpense.android.data.remote.dto.response.ExpenseResponseDto
import com.smartexpense.android.domain.repository.ExpenseRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

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

    override suspend fun analyzeExpense(imagePath: String, caption: String?): Result<com.smartexpense.android.data.remote.dto.response.AnalyzeExpenseResponseDto> {
        return try {
            val file = java.io.File(java.net.URI(imagePath).path)
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val body = okhttp3.MultipartBody.Part.createFormData("file", file.name, requestFile)
            val captionBody = caption?.let { it.toRequestBody("text/plain".toMediaTypeOrNull()) }
            
            val response = api.analyzeExpense(body, captionBody)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(Exception("Lỗi bóc tách hóa đơn: ${e.message}", e))
        }
    }

    override suspend fun getStatistics(period: String, fromDate: String?, toDate: String?): Result<com.smartexpense.android.data.remote.dto.response.StatisticsResponseDto> {
        return try {
            val response = api.getStatistics(period, fromDate, toDate)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(Exception("Lỗi tải thống kê: ${e.message}", e))
        }
    }

    override suspend fun deleteFile(url: String): Result<Unit> {
        return try {
            api.deleteFile(url)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Lỗi xóa file: ${e.message}", e))
        }
    }
}
