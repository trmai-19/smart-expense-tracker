package com.smartexpense.api.infrastructure.security

import com.smartexpense.api.domain.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {
        val user = userRepository.findByEmail(email)
            ?: throw UsernameNotFoundException("User Not Found with email: $email")

        return org.springframework.security.core.userdetails.User(
            user.email,
            user.passwordHash,
            emptyList()
        )
    }
}
