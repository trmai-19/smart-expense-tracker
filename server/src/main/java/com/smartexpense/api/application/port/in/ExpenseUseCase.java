package com.smartexpense.api.application.port.in;

import com.smartexpense.api.application.dto.request.ExpenseRequestDto;
import com.smartexpense.api.application.dto.response.ExpenseResponseDto;
import java.util.List;

public interface ExpenseUseCase {
    List<ExpenseResponseDto> getExpenses(String email);
    ExpenseResponseDto createExpense(String email, ExpenseRequestDto request);
}
