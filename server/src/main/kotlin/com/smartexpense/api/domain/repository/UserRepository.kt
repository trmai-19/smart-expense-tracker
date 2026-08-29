package com.smartexpense.api.domain.repository

import com.smartexpense.api.domain.model.User
import java.util.UUID

interface UserRepository {
    fun findById(id: UUID): User?
    fun findByEmail(email: String): User?
    fun existsByEmail(email: String): Boolean
    fun save(user: User): User
}
