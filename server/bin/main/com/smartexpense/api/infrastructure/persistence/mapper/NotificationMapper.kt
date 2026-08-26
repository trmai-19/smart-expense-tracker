package com.smartexpense.api.infrastructure.persistence.mapper

import com.smartexpense.api.domain.model.Notification
import com.smartexpense.api.infrastructure.persistence.entity.NotificationEntity
import com.smartexpense.api.infrastructure.persistence.entity.UserEntity
import org.springframework.stereotype.Component

@Component
class NotificationMapper {

    fun toDomain(entity: NotificationEntity) = Notification(
        id = entity.id,
        userId = entity.user.id!!,
        type = entity.type,
        content = entity.content,
        isRead = entity.isRead,
        createdAt = entity.createdAt
    )

    fun toEntity(domain: Notification): NotificationEntity {
        // Create a proxy UserEntity with only the ID to satisfy the FK relationship
        val userRef = UserEntity(
            id = domain.userId,
            email = "",
            passwordHash = "",
            displayName = ""
        )
        return NotificationEntity(
            id = domain.id,
            user = userRef,
            type = domain.type,
            content = domain.content,
            isRead = domain.isRead,
            createdAt = domain.createdAt
        )
    }
}
