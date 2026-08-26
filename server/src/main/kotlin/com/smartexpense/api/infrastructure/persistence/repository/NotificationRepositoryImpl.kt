package com.smartexpense.api.infrastructure.persistence.repository

import com.smartexpense.api.domain.model.Notification
import com.smartexpense.api.domain.repository.NotificationRepository
import com.smartexpense.api.infrastructure.persistence.mapper.NotificationMapper
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class NotificationRepositoryImpl(
    private val jpaRepository: NotificationJpaRepository,
    private val mapper: NotificationMapper
) : NotificationRepository {

    override fun findAllByUserIdOrderByCreatedAtDesc(userId: UUID): List<Notification> {
        return jpaRepository.findAllByUserIdOrderByCreatedAtDesc(userId)
            .map { mapper.toDomain(it) }
    }

    override fun findByIdAndUserId(id: UUID, userId: UUID): Notification? {
        return jpaRepository.findByIdAndUserId(id, userId)?.let { mapper.toDomain(it) }
    }

    override fun save(notification: Notification): Notification {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(notification)))
    }

    override fun markAllAsReadByUserId(userId: UUID) {
        jpaRepository.markAllAsReadByUserId(userId)
    }
}
