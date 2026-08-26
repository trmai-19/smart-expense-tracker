package com.smartexpense.api.presentation.controller

import com.smartexpense.api.application.dto.request.UpdateProfileRequestDto
import com.smartexpense.api.application.dto.response.UserProfileResponseDto
import com.smartexpense.api.application.port.`in`.ProfileUseCase
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(
    private val profileUseCase: ProfileUseCase
) {

    @GetMapping("/me")
    fun getMyProfile(authentication: Authentication): ResponseEntity<UserProfileResponseDto> {
        val email = authentication.name
        return ResponseEntity.ok(profileUseCase.getProfile(email))
    }

    @PutMapping("/me")
    fun updateProfile(
        authentication: Authentication,
        @RequestBody request: UpdateProfileRequestDto
    ): ResponseEntity<UserProfileResponseDto> {
        val email = authentication.name
        return ResponseEntity.ok(profileUseCase.updateProfile(email, request))
    }
}
