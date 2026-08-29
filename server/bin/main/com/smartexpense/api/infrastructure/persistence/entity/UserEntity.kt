package com.smartexpense.api.infrastructure.persistence.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "users")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @Column(nullable = false, unique = true, length = 255)
    var email: String,

    @Column(nullable = false, length = 255)
    var passwordHash: String,

    @Column(nullable = false, length = 100)
    var displayName: String,

    @Column(length = 500)
    var avatarUrl: String? = null,

    @Column(precision = 15, scale = 2)
    var monthlyBudget: BigDecimal? = BigDecimal.ZERO,

    var streakDays: Int? = 0,

    @Column(length = 20)
    var themeColor: String? = "#FFE600",

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime? = null,

    @UpdateTimestamp
    @Column(nullable = false)
    var updatedAt: LocalDateTime? = null
)
