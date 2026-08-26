package com.smartexpense.android.data.repository

import com.smartexpense.android.data.remote.api.NotificationApi
import com.smartexpense.android.data.remote.dto.response.NotificationResponseDto
import com.smartexpense.android.domain.repository.NotificationRepository

class NotificationRepositoryImpl(private val api: NotificationApi) : NotificationRepository {
    override suspend fun getNotifications(): Result<List<NotificationResponseDto>> {
        return try {
            val response = api.getNotifications()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(Exception("Lỗi tải thông báo: ${e.message}", e))
        }
    }

    override suspend fun markAsRead(id: String): Result<Unit> {
        return try {
            api.markAsRead(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Lỗi đánh dấu đã đọc: ${e.message}", e))
        }
    }

    override suspend fun markAllAsRead(): Result<Unit> {
        return try {
            api.markAllAsRead()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Lỗi đánh dấu tất cả đã đọc: ${e.message}", e))
        }
    }
}
