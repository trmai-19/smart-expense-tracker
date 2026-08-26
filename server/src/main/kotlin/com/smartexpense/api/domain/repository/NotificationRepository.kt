package com.smartexpense.api.domain.repository

import com.smartexpense.api.domain.model.Notification
import java.util.UUID

interface NotificationRepository {
    fun findAllByUserIdOrderByCreatedAtDesc(userId: UUID): List<Notification>
    fun findByIdAndUserId(id: UUID, userId: UUID): Notification?
    fun save(notification: Notification): Notification
    fun markAllAsReadByUserId(userId: UUID)
}
