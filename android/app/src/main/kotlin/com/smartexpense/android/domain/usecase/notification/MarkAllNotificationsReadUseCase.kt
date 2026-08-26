package com.smartexpense.android.domain.usecase.notification

import com.smartexpense.android.domain.repository.NotificationRepository

class MarkAllNotificationsReadUseCase(private val repository: NotificationRepository) {
    suspend fun execute(): Result<Unit> {
        return repository.markAllAsRead()
    }
}
