package com.smartexpense.api.domain.repository

import com.smartexpense.api.domain.model.Expense
import java.util.UUID

interface ExpenseRepository {
    fun findAllByUserIdOrderByExpenseDateDesc(userId: UUID): List<Expense>
    fun findByIdAndUserId(id: UUID, userId: UUID): Expense?
    fun save(expense: Expense): Expense
    fun deleteByIdAndUserId(id: UUID, userId: UUID)
}
