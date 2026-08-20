package com.smartexpense.api.application.usecase.user;

import com.smartexpense.api.application.dto.request.UpdateProfileRequestDto;
import com.smartexpense.api.application.dto.response.UserProfileResponseDto;
import com.smartexpense.api.application.port.in.ProfileUseCase;
import com.smartexpense.api.domain.model.User;
import com.smartexpense.api.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProfileUseCaseImpl implements ProfileUseCase {

    private final UserRepository userRepository;

    @Override
    public UserProfileResponseDto getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToDto(user);
    }

    @Override
    public UserProfileResponseDto updateProfile(String email, UpdateProfileRequestDto request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getDisplayName() != null) {
            user.setDisplayName(request.getDisplayName());
        }
        if (request.getAvatarUrl() != null) {
            user.setAvatarUrl(request.getAvatarUrl());
        }
        if (request.getMonthlyBudget() != null) {
            user.setMonthlyBudget(request.getMonthlyBudget());
        }
        if (request.getThemeColor() != null) {
            user.setThemeColor(request.getThemeColor());
        }
        
        user.setUpdatedAt(LocalDateTime.now());
        User savedUser = userRepository.save(user);

        return mapToDto(savedUser);
    }

    private UserProfileResponseDto mapToDto(User user) {
        return UserProfileResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .displayName(user.getDisplayName())
                .avatarUrl(user.getAvatarUrl())
                .monthlyBudget(user.getMonthlyBudget())
                .streakDays(user.getStreakDays())
                .themeColor(user.getThemeColor())
                .build();
    }
}
