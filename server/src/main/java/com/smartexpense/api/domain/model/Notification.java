package com.smartexpense.api.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    private UUID id;
    private UUID userId;
    private String type; // 'BUDGET_WARNING', 'AI_TIP', etc.
    private String content;
    private Boolean isRead;
    private LocalDateTime createdAt;
}
