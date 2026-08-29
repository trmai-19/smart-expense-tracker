package com.smartexpense.android.domain.usecase.notification

import com.smartexpense.android.domain.repository.NotificationRepository

class MarkNotificationReadUseCase(private val repository: NotificationRepository) {
    suspend fun execute(id: String): Result<Unit> {
        return repository.markAsRead(id)
    }
}
