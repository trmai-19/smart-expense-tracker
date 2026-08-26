package com.smartexpense.api.application.usecase.expense

import com.smartexpense.api.application.dto.request.ExpenseRequestDto
import com.smartexpense.api.application.dto.response.ExpenseResponseDto
import com.smartexpense.api.application.port.`in`.ExpenseUseCase
import com.smartexpense.api.domain.model.Expense
import com.smartexpense.api.domain.model.User
import com.smartexpense.api.domain.repository.ExpenseRepository
import com.smartexpense.api.domain.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class ExpenseUseCaseImpl(
    private val expenseRepository: ExpenseRepository,
    private val userRepository: UserRepository
) : ExpenseUseCase {

    override fun getExpenses(email: String): List<ExpenseResponseDto> {
        val user = getUserByEmail(email)
        return expenseRepository.findAllByUserIdOrderByExpenseDateDesc(user.id!!)
            .map { mapToDto(it) }
    }

    override fun createExpense(email: String, request: ExpenseRequestDto): ExpenseResponseDto {
        val user = getUserByEmail(email)

        val newExpense = Expense(
            userId = user.id!!,
            amount = request.amount,
            category = request.category,
            photoUrl = request.photoUrl,
            caption = request.caption,
            expenseDate = request.expenseDate
        )

        return mapToDto(expenseRepository.save(newExpense))
    }

    private fun getUserByEmail(email: String): User {
        return userRepository.findByEmail(email)
            ?: throw RuntimeException("User not found")
    }

    private fun mapToDto(expense: Expense) = ExpenseResponseDto(
        id = expense.id!!,
        amount = expense.amount,
        category = expense.category,
        photoUrl = expense.photoUrl,
        caption = expense.caption,
        expenseDate = expense.expenseDate
    )
}
