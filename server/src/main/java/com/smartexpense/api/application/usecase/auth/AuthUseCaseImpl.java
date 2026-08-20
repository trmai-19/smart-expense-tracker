package com.smartexpense.api.application.usecase.auth;

import com.smartexpense.api.application.dto.request.LoginRequestDto;
import com.smartexpense.api.application.dto.request.RegisterRequestDto;
import com.smartexpense.api.application.dto.response.AuthResponseDto;
import com.smartexpense.api.application.port.in.AuthUseCase;
import com.smartexpense.api.application.port.out.JwtTokenPort;
import com.smartexpense.api.application.port.out.PasswordEncoderPort;
import com.smartexpense.api.domain.exception.AuthException;
import com.smartexpense.api.domain.model.User;
import com.smartexpense.api.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthUseCaseImpl implements AuthUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoderPort passwordEncoderPort;
    private final JwtTokenPort jwtTokenPort;

    @Override
    public AuthResponseDto login(LoginRequestDto request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthException("Invalid email or password"));

        if (!passwordEncoderPort.matches(request.getPassword(), user.getPasswordHash())) {
            throw new AuthException("Invalid email or password");
        }

        String token = jwtTokenPort.generateToken(user);
        return buildAuthResponse(token, user);
    }

    @Override
    public AuthResponseDto register(RegisterRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AuthException("Email is already in use");
        }

        User newUser = User.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoderPort.encode(request.getPassword()))
                .displayName(request.getDisplayName())
                .monthlyBudget(BigDecimal.ZERO)
                .streakDays(0)
                .themeColor("#FFE600")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(newUser);
        String token = jwtTokenPort.generateToken(savedUser);
        
        return buildAuthResponse(token, savedUser);
    }

    private AuthResponseDto buildAuthResponse(String token, User user) {
        return AuthResponseDto.builder()
                .token(token)
                .user(AuthResponseDto.UserDto.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .displayName(user.getDisplayName())
                        .avatarUrl(user.getAvatarUrl())
                        .monthlyBudget(user.getMonthlyBudget())
                        .streakDays(user.getStreakDays())
                        .themeColor(user.getThemeColor())
                        .build())
                .build();
    }
}
