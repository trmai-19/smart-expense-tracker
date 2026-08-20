package com.smartexpense.api.application.port.in;

import com.smartexpense.api.application.dto.response.NotificationResponseDto;
import java.util.List;
import java.util.UUID;

public interface NotificationUseCase {
    List<NotificationResponseDto> getNotifications(String email);
    void markAsRead(String email, UUID notificationId);
    void markAllAsRead(String email);
}
