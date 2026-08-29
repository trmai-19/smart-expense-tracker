package com.smartexpense.api.infrastructure.persistence.repository

import com.smartexpense.api.infrastructure.persistence.entity.ChatMessageEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ChatMessageJpaRepository : JpaRepository<ChatMessageEntity, UUID> {
    fun findTop20ByUser_IdOrderByCreatedAtAsc(userId: UUID): List<ChatMessageEntity>
}
