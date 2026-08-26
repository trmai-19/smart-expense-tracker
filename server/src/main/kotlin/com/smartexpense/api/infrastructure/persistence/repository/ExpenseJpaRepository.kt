package com.smartexpense.api.infrastructure.persistence.repository

import com.smartexpense.api.infrastructure.persistence.entity.ExpenseEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ExpenseJpaRepository : JpaRepository<ExpenseEntity, UUID> {
    fun findAllByUserIdOrderByExpenseDateDesc(userId: UUID): List<ExpenseEntity>

    @Query("SELECT e FROM ExpenseEntity e WHERE e.id = :id AND e.user.id = :userId")
    fun findByIdAndUserId(@Param("id") id: UUID, @Param("userId") userId: UUID): ExpenseEntity?

    fun deleteByIdAndUserId(id: UUID, userId: UUID)
}
