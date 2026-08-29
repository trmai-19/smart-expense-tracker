package com.smartexpense.api.infrastructure.persistence.repository

import com.smartexpense.api.domain.model.Expense
import com.smartexpense.api.domain.repository.ExpenseRepository
import com.smartexpense.api.infrastructure.persistence.mapper.ExpenseMapper
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class ExpenseRepositoryImpl(
    private val jpaRepository: ExpenseJpaRepository,
    private val mapper: ExpenseMapper
) : ExpenseRepository {

    override fun findAllByUserIdOrderByExpenseDateDesc(userId: UUID): List<Expense> {
        return jpaRepository.findAllByUserIdOrderByExpenseDateDesc(userId)
            .map { mapper.toDomain(it) }
    }

    override fun findByIdAndUserId(id: UUID, userId: UUID): Expense? {
        return jpaRepository.findByIdAndUserId(id, userId)?.let { mapper.toDomain(it) }
    }

    override fun save(expense: Expense): Expense {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(expense)))
    }

    override fun deleteByIdAndUserId(id: UUID, userId: UUID) {
        jpaRepository.deleteByIdAndUserId(id, userId)
    }
}
