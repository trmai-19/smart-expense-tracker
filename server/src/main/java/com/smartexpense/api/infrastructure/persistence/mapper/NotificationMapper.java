package com.smartexpense.api.infrastructure.persistence.mapper;

import com.smartexpense.api.domain.model.Notification;
import com.smartexpense.api.infrastructure.persistence.entity.NotificationEntity;
import com.smartexpense.api.infrastructure.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public Notification toDomain(NotificationEntity entity) {
        if (entity == null) {
            return null;
        }
        return Notification.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .type(entity.getType())
                .content(entity.getContent())
                .isRead(entity.getIsRead())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public NotificationEntity toEntity(Notification domain) {
        if (domain == null) {
            return null;
        }
        UserEntity user = new UserEntity();
        user.setId(domain.getUserId());

        return NotificationEntity.builder()
                .id(domain.getId())
                .user(user)
                .type(domain.getType())
                .content(domain.getContent())
                .isRead(domain.getIsRead())
                .createdAt(domain.getCreatedAt())
                .build();
    }
}
