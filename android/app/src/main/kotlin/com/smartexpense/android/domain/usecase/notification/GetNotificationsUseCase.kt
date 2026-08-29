package com.smartexpense.android.domain.usecase.notification

import com.smartexpense.android.data.remote.dto.response.NotificationResponseDto
import com.smartexpense.android.domain.repository.NotificationRepository

class GetNotificationsUseCase(private val repository: NotificationRepository) {
    suspend fun execute(): Result<List<NotificationResponseDto>> {
        return repository.getNotifications()
    }
}
