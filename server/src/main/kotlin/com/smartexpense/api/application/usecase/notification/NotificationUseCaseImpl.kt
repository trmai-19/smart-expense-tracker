package com.smartexpense.api.application.usecase.notification

import com.smartexpense.api.application.dto.response.NotificationResponseDto
import com.smartexpense.api.application.port.`in`.NotificationUseCase
import com.smartexpense.api.domain.model.Notification
import com.smartexpense.api.domain.model.User
import com.smartexpense.api.domain.repository.NotificationRepository
import com.smartexpense.api.domain.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class NotificationUseCaseImpl(
    private val notificationRepository: NotificationRepository,
    private val userRepository: UserRepository
) : NotificationUseCase {

    override fun getNotifications(email: String): List<NotificationResponseDto> {
        val user = getUserByEmail(email)
        return notificationRepository.findAllByUserIdOrderByCreatedAtDesc(user.id!!)
            .map { mapToDto(it) }
    }

    @Transactional
    override fun markAsRead(email: String, notificationId: UUID) {
        val user = getUserByEmail(email)
        val notification = notificationRepository.findByIdAndUserId(notificationId, user.id!!)
            ?: throw RuntimeException("Notification not found")

        notificationRepository.save(notification.copy(isRead = true))
    }

    @Transactional
    override fun markAllAsRead(email: String) {
        val user = getUserByEmail(email)
        notificationRepository.markAllAsReadByUserId(user.id!!)
    }

    private fun getUserByEmail(email: String): User {
        return userRepository.findByEmail(email)
            ?: throw RuntimeException("User not found")
    }

    private fun mapToDto(notification: Notification) = NotificationResponseDto(
        id = notification.id!!,
        type = notification.type,
        content = notification.content,
        isRead = notification.isRead,
        createdAt = notification.createdAt!!
    )
}
