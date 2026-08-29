package com.smartexpense.api.presentation.controller

import com.smartexpense.api.application.dto.request.ExpenseRequestDto
import com.smartexpense.api.application.dto.response.ExpenseResponseDto
import com.smartexpense.api.application.port.`in`.ExpenseUseCase
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import com.smartexpense.api.application.dto.response.AnalyzeExpenseResponseDto
import com.smartexpense.api.application.dto.response.StatisticsResponseDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

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

    @PostMapping("/analyze")
    fun analyzeExpense(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("caption", required = false) caption: String?,
        authentication: Authentication
    ): ResponseEntity<AnalyzeExpenseResponseDto> {
        val email = authentication.name
        return ResponseEntity.ok(expenseUseCase.analyzeExpense(email, file, caption))
    }

    @GetMapping("/statistics")
    fun getStatistics(
        @RequestParam(defaultValue = "MONTH") period: String,
        @RequestParam(required = false) fromDate: String?,
        @RequestParam(required = false) toDate: String?,
        authentication: Authentication
    ): ResponseEntity<StatisticsResponseDto> {
        val email = authentication.name
        return ResponseEntity.ok(expenseUseCase.getStatistics(email, period, fromDate, toDate))
    }
}
