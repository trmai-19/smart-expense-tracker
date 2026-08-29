package com.smartexpense.android.data.remote.api

import com.smartexpense.android.data.remote.dto.request.ExpenseRequestDto
import com.smartexpense.android.data.remote.dto.response.ExpenseResponseDto
import com.smartexpense.android.data.remote.dto.response.AnalyzeExpenseResponseDto
import com.smartexpense.android.data.remote.dto.response.StatisticsResponseDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Multipart
import retrofit2.http.Part
import retrofit2.http.Query
import retrofit2.http.DELETE

interface ExpenseApi {
    @GET("api/expenses")
    suspend fun getExpenses(): List<ExpenseResponseDto>

    @POST("api/expenses")
    suspend fun createExpense(@Body request: ExpenseRequestDto): ExpenseResponseDto

    @Multipart
    @POST("api/expenses/analyze")
    suspend fun analyzeExpense(
        @Part file: MultipartBody.Part,
        @Part("caption") caption: RequestBody?
    ): AnalyzeExpenseResponseDto

    @GET("api/expenses/statistics")
    suspend fun getStatistics(
        @Query("period") period: String,
        @Query("fromDate") fromDate: String? = null,
        @Query("toDate") toDate: String? = null
    ): StatisticsResponseDto

    @DELETE("api/files/delete")
    suspend fun deleteFile(@Query("url") url: String)
}
