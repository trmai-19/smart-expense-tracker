package com.smartexpense.api.application.usecase.auth

import com.smartexpense.api.application.dto.request.LoginRequestDto
import com.smartexpense.api.application.dto.request.RegisterRequestDto
import com.smartexpense.api.application.dto.response.AuthResponseDto
import com.smartexpense.api.application.port.`in`.AuthUseCase
import com.smartexpense.api.application.port.out.JwtTokenPort
import com.smartexpense.api.application.port.out.PasswordEncoderPort
import com.smartexpense.api.domain.exception.AuthException
import com.smartexpense.api.domain.model.User
import com.smartexpense.api.domain.repository.UserRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class AuthUseCaseImpl(
    private val userRepository: UserRepository,
    private val passwordEncoderPort: PasswordEncoderPort,
    private val jwtTokenPort: JwtTokenPort
) : AuthUseCase {

    override fun login(request: LoginRequestDto): AuthResponseDto {
        val user = userRepository.findByEmail(request.email)
            ?: throw AuthException("Invalid email or password")

        if (!passwordEncoderPort.matches(request.password, user.passwordHash)) {
            throw AuthException("Invalid email or password")
        }

        val token = jwtTokenPort.generateToken(user)
        return AuthResponseDto(
            token = token,
            userId = user.id!!,
            displayName = user.displayName,
            email = user.email
        )
    }

    override fun register(request: RegisterRequestDto): AuthResponseDto {
        if (userRepository.existsByEmail(request.email)) {
            throw AuthException("Email is already in use")
        }

        val newUser = User(
            email = request.email,
            passwordHash = passwordEncoderPort.encode(request.password),
            displayName = request.displayName,
            monthlyBudget = BigDecimal.ZERO,
            streakDays = 0,
            themeColor = "#FFE600",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        val savedUser = userRepository.save(newUser)
        val token = jwtTokenPort.generateToken(savedUser)

        return AuthResponseDto(
            token = token,
            userId = savedUser.id!!,
            displayName = savedUser.displayName,
            email = savedUser.email
        )
    }
}
