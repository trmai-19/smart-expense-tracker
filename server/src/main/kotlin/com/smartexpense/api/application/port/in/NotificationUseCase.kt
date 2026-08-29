package com.smartexpense.api.application.port.`in`

import com.smartexpense.api.application.dto.response.NotificationResponseDto
import java.util.UUID

interface NotificationUseCase {
    fun getNotifications(email: String): List<NotificationResponseDto>
    fun markAsRead(email: String, notificationId: UUID)
    fun markAllAsRead(email: String)
}
