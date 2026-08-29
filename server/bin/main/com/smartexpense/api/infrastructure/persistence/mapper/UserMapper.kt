package com.smartexpense.api.infrastructure.persistence.mapper

import com.smartexpense.api.domain.model.User
import com.smartexpense.api.infrastructure.persistence.entity.UserEntity
import org.springframework.stereotype.Component

@Component
class UserMapper {

    fun toDomain(entity: UserEntity) = User(
        id = entity.id,
        email = entity.email,
        passwordHash = entity.passwordHash,
        displayName = entity.displayName,
        avatarUrl = entity.avatarUrl,
        monthlyBudget = entity.monthlyBudget,
        streakDays = entity.streakDays,
        themeColor = entity.themeColor,
        createdAt = entity.createdAt,
        updatedAt = entity.updatedAt
    )

    fun toEntity(domain: User) = UserEntity(
        id = domain.id,
        email = domain.email,
        passwordHash = domain.passwordHash,
        displayName = domain.displayName,
        avatarUrl = domain.avatarUrl,
        monthlyBudget = domain.monthlyBudget,
        streakDays = domain.streakDays,
        themeColor = domain.themeColor,
        createdAt = domain.createdAt,
        updatedAt = domain.updatedAt
    )
}
