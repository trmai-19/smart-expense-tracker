package com.smartexpense.api.presentation.controller;

import com.smartexpense.api.application.dto.response.NotificationResponseDto;
import com.smartexpense.api.application.port.in.NotificationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationUseCase notificationUseCase;

    @GetMapping
    public ResponseEntity<List<NotificationResponseDto>> getNotifications(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(notificationUseCase.getNotifications(email));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable UUID id, Authentication authentication) {
        String email = authentication.getName();
        notificationUseCase.markAsRead(email, id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead(Authentication authentication) {
        String email = authentication.getName();
        notificationUseCase.markAllAsRead(email);
        return ResponseEntity.ok().build();
    }
}
