package com.smartexpense.api.presentation.controller

import com.smartexpense.api.application.dto.request.LoginRequestDto
import com.smartexpense.api.application.dto.request.RegisterRequestDto
import com.smartexpense.api.application.dto.response.AuthResponseDto
import com.smartexpense.api.application.port.`in`.AuthUseCase
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authUseCase: AuthUseCase
) {

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequestDto): ResponseEntity<AuthResponseDto> {
        val response = authUseCase.login(request)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/register")
    fun register(@Valid @RequestBody request: RegisterRequestDto): ResponseEntity<AuthResponseDto> {
        val response = authUseCase.register(request)
        return ResponseEntity.ok(response)
    }
}
