package com.smartexpense.android.data.remote.api;

import com.smartexpense.android.data.remote.dto.request.ExpenseRequestDto;
import com.smartexpense.android.data.remote.dto.response.ExpenseResponseDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ExpenseApi {
    @GET("api/expenses")
    Call<List<ExpenseResponseDto>> getExpenses();

    @POST("api/expenses")
    Call<ExpenseResponseDto> createExpense(@Body ExpenseRequestDto request);
}
