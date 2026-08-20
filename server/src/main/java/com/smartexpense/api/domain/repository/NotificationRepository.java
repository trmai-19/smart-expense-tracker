package com.smartexpense.api.domain.repository;

import com.smartexpense.api.domain.model.Notification;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository {
    List<Notification> findAllByUserIdOrderByCreatedAtDesc(UUID userId);
    Optional<Notification> findByIdAndUserId(UUID id, UUID userId);
    Notification save(Notification notification);
    void markAllAsReadByUserId(UUID userId);
}
