package com.smartexpense.api.infrastructure.persistence.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(
    name = "expenses",
    indexes = [
        Index(name = "idx_expenses_user_date", columnList = "user_id, expense_date")
    ]
)
class ExpenseEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: UserEntity,

    @Column(nullable = false, precision = 15, scale = 2)
    var amount: BigDecimal,

    @Column(nullable = false, length = 100)
    var category: String,

    @Column(nullable = false, length = 500)
    var photoUrl: String,

    @Column(length = 255)
    var caption: String? = null,

    @Column(nullable = false)
    var expenseDate: LocalDateTime,

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime? = null,

    @UpdateTimestamp
    @Column(nullable = false)
    var updatedAt: LocalDateTime? = null
)
