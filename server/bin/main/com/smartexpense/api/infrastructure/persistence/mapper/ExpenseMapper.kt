package com.smartexpense.api.infrastructure.persistence.mapper

import com.smartexpense.api.domain.model.Expense
import com.smartexpense.api.infrastructure.persistence.entity.ExpenseEntity
import com.smartexpense.api.infrastructure.persistence.entity.UserEntity
import org.springframework.stereotype.Component

@Component
class ExpenseMapper {

    fun toDomain(entity: ExpenseEntity) = Expense(
        id = entity.id,
        userId = entity.user.id!!,
        amount = entity.amount,
        category = entity.category,
        photoUrl = entity.photoUrl,
        caption = entity.caption,
        expenseDate = entity.expenseDate,
        createdAt = entity.createdAt,
        updatedAt = entity.updatedAt
    )

    fun toEntity(domain: Expense): ExpenseEntity {
        // Create a proxy UserEntity with only the ID to satisfy the FK relationship
        val userRef = UserEntity(
            id = domain.userId,
            email = "",
            passwordHash = "",
            displayName = ""
        )
        return ExpenseEntity(
            id = domain.id,
            user = userRef,
            amount = domain.amount,
            category = domain.category,
            photoUrl = domain.photoUrl,
            caption = domain.caption,
            expenseDate = domain.expenseDate,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt
        )
    }
}
