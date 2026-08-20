package com.smartexpense.api.application.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class NotificationResponseDto {
    private UUID id;
    private String type;
    private String content;
    private Boolean isRead;
    private LocalDateTime createdAt;
}
