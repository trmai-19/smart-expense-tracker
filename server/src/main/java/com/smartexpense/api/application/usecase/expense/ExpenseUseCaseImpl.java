package com.smartexpense.api.application.usecase.expense;

import com.smartexpense.api.application.dto.request.ExpenseRequestDto;
import com.smartexpense.api.application.dto.response.ExpenseResponseDto;
import com.smartexpense.api.application.port.in.ExpenseUseCase;
import com.smartexpense.api.domain.model.Expense;
import com.smartexpense.api.domain.model.User;
import com.smartexpense.api.domain.repository.ExpenseRepository;
import com.smartexpense.api.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseUseCaseImpl implements ExpenseUseCase {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    @Override
    public List<ExpenseResponseDto> getExpenses(String email) {
        User user = getUserByEmail(email);
        return expenseRepository.findAllByUserIdOrderByExpenseDateDesc(user.getId())
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ExpenseResponseDto createExpense(String email, ExpenseRequestDto request) {
        User user = getUserByEmail(email);

        Expense newExpense = Expense.builder()
                .userId(user.getId())
                .amount(request.getAmount())
                .category(request.getCategory())
                .photoUrl(request.getPhotoUrl())
                .caption(request.getCaption())
                .expenseDate(request.getExpenseDate())
                .build();

        Expense savedExpense = expenseRepository.save(newExpense);
        return mapToDto(savedExpense);
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private ExpenseResponseDto mapToDto(Expense expense) {
        return ExpenseResponseDto.builder()
                .id(expense.getId())
                .amount(expense.getAmount())
                .category(expense.getCategory())
                .photoUrl(expense.getPhotoUrl())
                .caption(expense.getCaption())
                .expenseDate(expense.getExpenseDate())
                .createdAt(expense.getCreatedAt())
                .build();
    }
}
