package com.smartexpense.api.infrastructure.persistence.repository

import com.smartexpense.api.domain.model.ChatMessage
import com.smartexpense.api.domain.repository.ChatRepository
import com.smartexpense.api.infrastructure.persistence.entity.ChatMessageEntity
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class ChatRepositoryImpl(
    private val chatMessageJpaRepository: ChatMessageJpaRepository,
    private val userJpaRepository: UserJpaRepository
) : ChatRepository {

    override fun saveMessage(userId: UUID, role: String, content: String): ChatMessage {
        val user = userJpaRepository.findById(userId)
            .orElseThrow { RuntimeException("User not found: $userId") }

        val entity = ChatMessageEntity(
            user = user,
            role = role,
            content = content
        )
        val saved = chatMessageJpaRepository.save(entity)
        return toDomain(saved)
    }

    override fun findHistoryByUserId(userId: UUID): List<ChatMessage> {
        return chatMessageJpaRepository.findTop20ByUser_IdOrderByCreatedAtAsc(userId)
            .map { toDomain(it) }
    }

    private fun toDomain(entity: ChatMessageEntity) = ChatMessage(
        id = entity.id,
        userId = entity.user.id!!,
        role = entity.role,
        content = entity.content,
        createdAt = entity.createdAt
    )
}
