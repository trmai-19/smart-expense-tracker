package com.smartexpense.api.application.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class AuthResponseDto {
    private String token;
    private UserDto user;

    @Data
    @Builder
    public static class UserDto {
        private UUID id;
        private String email;
        private String displayName;
        private String avatarUrl;
        private BigDecimal monthlyBudget;
        private Integer streakDays;
        private String themeColor;
    }
}
