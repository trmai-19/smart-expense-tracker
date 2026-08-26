package com.smartexpense.api.presentation.controller

import com.smartexpense.api.application.dto.request.ExpenseRequestDto
import com.smartexpense.api.application.dto.response.ExpenseResponseDto
import com.smartexpense.api.application.port.`in`.ExpenseUseCase
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/expenses")
class ExpenseController(
    private val expenseUseCase: ExpenseUseCase
) {

    @GetMapping
    fun getExpenses(authentication: Authentication): ResponseEntity<List<ExpenseResponseDto>> {
        val email = authentication.name
        return ResponseEntity.ok(expenseUseCase.getExpenses(email))
    }

    @PostMapping
    fun createExpense(
        @Valid @RequestBody request: ExpenseRequestDto,
        authentication: Authentication
    ): ResponseEntity<ExpenseResponseDto> {
        val email = authentication.name
        return ResponseEntity.ok(expenseUseCase.createExpense(email, request))
    }
}
