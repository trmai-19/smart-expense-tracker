package com.smartexpense.android.data.remote.api

import com.smartexpense.android.data.remote.dto.request.ExpenseRequestDto
import com.smartexpense.android.data.remote.dto.response.ExpenseResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ExpenseApi {
    @GET("api/expenses")
    suspend fun getExpenses(): List<ExpenseResponseDto>

    @POST("api/expenses")
    suspend fun createExpense(@Body request: ExpenseRequestDto): ExpenseResponseDto
}
