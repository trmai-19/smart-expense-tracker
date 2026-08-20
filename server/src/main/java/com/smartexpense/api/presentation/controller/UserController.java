package com.smartexpense.api.presentation.controller;

import com.smartexpense.api.application.dto.request.UpdateProfileRequestDto;
import com.smartexpense.api.application.dto.response.UserProfileResponseDto;
import com.smartexpense.api.application.port.in.ProfileUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final ProfileUseCase profileUseCase;

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponseDto> getMyProfile(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(profileUseCase.getProfile(email));
    }

    @PutMapping("/me")
    public ResponseEntity<UserProfileResponseDto> updateProfile(
            Authentication authentication,
            @RequestBody UpdateProfileRequestDto request) {
        String email = authentication.getName();
        return ResponseEntity.ok(profileUseCase.updateProfile(email, request));
    }
}
