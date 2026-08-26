package com.smartexpense.android.domain.repository

import com.smartexpense.android.data.remote.dto.response.NotificationResponseDto

interface NotificationRepository {
    suspend fun getNotifications(): Result<List<NotificationResponseDto>>
    suspend fun markAsRead(id: String): Result<Unit>
    suspend fun markAllAsRead(): Result<Unit>
}
