package com.smartexpense.api.infrastructure.persistence.mapper;

import com.smartexpense.api.domain.model.User;
import com.smartexpense.api.infrastructure.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        return User.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .passwordHash(entity.getPasswordHash())
                .displayName(entity.getDisplayName())
                .avatarUrl(entity.getAvatarUrl())
                .monthlyBudget(entity.getMonthlyBudget())
                .streakDays(entity.getStreakDays())
                .themeColor(entity.getThemeColor())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public UserEntity toEntity(User domain) {
        if (domain == null) {
            return null;
        }
        return UserEntity.builder()
                .id(domain.getId())
                .email(domain.getEmail())
                .passwordHash(domain.getPasswordHash())
                .displayName(domain.getDisplayName())
                .avatarUrl(domain.getAvatarUrl())
                .monthlyBudget(domain.getMonthlyBudget())
                .streakDays(domain.getStreakDays())
                .themeColor(domain.getThemeColor())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}
