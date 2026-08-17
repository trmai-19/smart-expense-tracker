package com.smartexpense.api.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private UUID id;
    private String email;
    private String passwordHash;
    private String displayName;
    private String avatarUrl;
    private BigDecimal monthlyBudget;
    private Integer streakDays;
    private String themeColor;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
