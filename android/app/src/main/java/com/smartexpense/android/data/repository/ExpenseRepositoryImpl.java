package com.smartexpense.android.data.repository;

import com.smartexpense.android.data.remote.api.ExpenseApi;
import com.smartexpense.android.data.remote.dto.request.ExpenseRequestDto;
import com.smartexpense.android.data.remote.dto.response.ExpenseResponseDto;
import com.smartexpense.android.domain.repository.ExpenseRepository;
import com.smartexpense.android.domain.repository.ResultCallback;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpenseRepositoryImpl implements ExpenseRepository {

    private final ExpenseApi api;

    public ExpenseRepositoryImpl(ExpenseApi api) {
        this.api = api;
    }

    @Override
    public void getExpenses(ResultCallback<List<ExpenseResponseDto>> callback) {
        api.getExpenses().enqueue(new Callback<List<ExpenseResponseDto>>() {
            @Override
            public void onResponse(Call<List<ExpenseResponseDto>> call, Response<List<ExpenseResponseDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Lỗi tải lịch sử chi tiêu: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<ExpenseResponseDto>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    @Override
    public void createExpense(ExpenseRequestDto request, ResultCallback<ExpenseResponseDto> callback) {
        api.createExpense(request).enqueue(new Callback<ExpenseResponseDto>() {
            @Override
            public void onResponse(Call<ExpenseResponseDto> call, Response<ExpenseResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Lỗi tạo chi tiêu: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ExpenseResponseDto> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
}
