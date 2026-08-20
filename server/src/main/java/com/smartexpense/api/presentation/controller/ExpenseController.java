package com.smartexpense.api.presentation.controller;

import com.smartexpense.api.application.dto.request.ExpenseRequestDto;
import com.smartexpense.api.application.dto.response.ExpenseResponseDto;
import com.smartexpense.api.application.port.in.ExpenseUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseUseCase expenseUseCase;

    @GetMapping
    public ResponseEntity<List<ExpenseResponseDto>> getExpenses(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(expenseUseCase.getExpenses(email));
    }

    @PostMapping
    public ResponseEntity<ExpenseResponseDto> createExpense(
            @Valid @RequestBody ExpenseRequestDto request,
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(expenseUseCase.createExpense(email, request));
    }
}
