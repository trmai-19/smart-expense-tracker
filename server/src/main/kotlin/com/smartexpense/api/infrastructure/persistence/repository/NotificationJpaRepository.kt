package com.smartexpense.api.infrastructure.persistence.repository

import com.smartexpense.api.infrastructure.persistence.entity.NotificationEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface NotificationJpaRepository : JpaRepository<NotificationEntity, UUID> {
    fun findAllByUserIdOrderByCreatedAtDesc(userId: UUID): List<NotificationEntity>

    @Query("SELECT n FROM NotificationEntity n WHERE n.id = :id AND n.user.id = :userId")
    fun findByIdAndUserId(@Param("id") id: UUID, @Param("userId") userId: UUID): NotificationEntity?

    @Modifying
    @Query("UPDATE NotificationEntity n SET n.isRead = true WHERE n.user.id = :userId")
    fun markAllAsReadByUserId(@Param("userId") userId: UUID)
}
