package com.smartexpense.android.domain.usecase.expense;

import com.smartexpense.android.data.remote.dto.response.ExpenseResponseDto;
import com.smartexpense.android.domain.repository.ExpenseRepository;
import com.smartexpense.android.domain.repository.ResultCallback;

import java.util.List;

public class GetExpensesUseCase {
    private final ExpenseRepository repository;

    public GetExpensesUseCase(ExpenseRepository repository) {
        this.repository = repository;
    }

    public void execute(ResultCallback<List<ExpenseResponseDto>> callback) {
        repository.getExpenses(callback);
    }
}
