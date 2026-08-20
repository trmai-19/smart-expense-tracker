package com.smartexpense.api.infrastructure.persistence.repository;

import com.smartexpense.api.domain.model.Notification;
import com.smartexpense.api.domain.repository.NotificationRepository;
import com.smartexpense.api.infrastructure.persistence.entity.NotificationEntity;
import com.smartexpense.api.infrastructure.persistence.mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {

    private final NotificationJpaRepository jpaRepository;
    private final NotificationMapper mapper;

    @Override
    public List<Notification> findAllByUserIdOrderByCreatedAtDesc(UUID userId) {
        return jpaRepository.findAllByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Notification> findByIdAndUserId(UUID id, UUID userId) {
        return jpaRepository.findByIdAndUserId(id, userId)
                .map(mapper::toDomain);
    }

    @Override
    public Notification save(Notification notification) {
        NotificationEntity entity = mapper.toEntity(notification);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public void markAllAsReadByUserId(UUID userId) {
        jpaRepository.markAllAsReadByUserId(userId);
    }
}
