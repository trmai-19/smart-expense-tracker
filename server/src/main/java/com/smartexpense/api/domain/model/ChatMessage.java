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
public class ChatMessage {
    private UUID id;
    private UUID userId;
    private String role; // 'USER' or 'AI'
    private String content;
    private LocalDateTime createdAt;
}
