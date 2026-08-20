package com.smartexpense.android.domain.usecase.expense;

import com.smartexpense.android.data.remote.dto.request.ExpenseRequestDto;
import com.smartexpense.android.data.remote.dto.response.ExpenseResponseDto;
import com.smartexpense.android.domain.repository.ExpenseRepository;
import com.smartexpense.android.domain.repository.ResultCallback;

public class CreateExpenseUseCase {
    private final ExpenseRepository repository;

    public CreateExpenseUseCase(ExpenseRepository repository) {
        this.repository = repository;
    }

    public void execute(double amount, String category, String photoUrl, String caption, String expenseDate, ResultCallback<ExpenseResponseDto> callback) {
        ExpenseRequestDto request = new ExpenseRequestDto(amount, category, photoUrl, caption, expenseDate);
        repository.createExpense(request, callback);
    }
}
