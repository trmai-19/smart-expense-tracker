package com.smartexpense.api.presentation.controller

import com.smartexpense.api.application.dto.response.NotificationResponseDto
import com.smartexpense.api.application.port.`in`.NotificationUseCase
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/notifications")
class NotificationController(
    private val notificationUseCase: NotificationUseCase
) {

    @GetMapping
    fun getNotifications(authentication: Authentication): ResponseEntity<List<NotificationResponseDto>> {
        val email = authentication.name
        return ResponseEntity.ok(notificationUseCase.getNotifications(email))
    }

    @PatchMapping("/{id}/read")
    fun markAsRead(@PathVariable id: UUID, authentication: Authentication): ResponseEntity<Void> {
        val email = authentication.name
        notificationUseCase.markAsRead(email, id)
        return ResponseEntity.ok().build()
    }

    @PatchMapping("/read-all")
    fun markAllAsRead(authentication: Authentication): ResponseEntity<Void> {
        val email = authentication.name
        notificationUseCase.markAllAsRead(email)
        return ResponseEntity.ok().build()
    }
}
