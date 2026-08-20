package com.smartexpense.api.presentation.controller;

import com.smartexpense.api.application.dto.request.LoginRequestDto;
import com.smartexpense.api.application.dto.request.RegisterRequestDto;
import com.smartexpense.api.application.dto.response.AuthResponseDto;
import com.smartexpense.api.application.port.in.AuthUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthUseCase authUseCase;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        AuthResponseDto response = authUseCase.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody RegisterRequestDto request) {
        AuthResponseDto response = authUseCase.register(request);
        return ResponseEntity.ok(response);
    }
}
