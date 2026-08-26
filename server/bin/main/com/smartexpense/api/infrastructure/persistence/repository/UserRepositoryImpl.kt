package com.smartexpense.api.infrastructure.persistence.repository

import com.smartexpense.api.domain.model.User
import com.smartexpense.api.domain.repository.UserRepository
import com.smartexpense.api.infrastructure.persistence.mapper.UserMapper
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class UserRepositoryImpl(
    private val jpaRepository: UserJpaRepository,
    private val mapper: UserMapper
) : UserRepository {

    override fun findById(id: UUID): User? {
        return jpaRepository.findByIdOrNull(id)?.let { mapper.toDomain(it) }
    }

    override fun findByEmail(email: String): User? {
        return jpaRepository.findByEmail(email)?.let { mapper.toDomain(it) }
    }

    override fun existsByEmail(email: String): Boolean {
        return jpaRepository.existsByEmail(email)
    }

    override fun save(user: User): User {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(user)))
    }
}
