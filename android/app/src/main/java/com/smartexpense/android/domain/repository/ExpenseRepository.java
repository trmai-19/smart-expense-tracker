package com.smartexpense.android.domain.repository;

import com.smartexpense.android.data.remote.dto.request.ExpenseRequestDto;
import com.smartexpense.android.data.remote.dto.response.ExpenseResponseDto;

import java.util.List;

public interface ExpenseRepository {
    void getExpenses(ResultCallback<List<ExpenseResponseDto>> callback);
    void createExpense(ExpenseRequestDto request, ResultCallback<ExpenseResponseDto> callback);
}
