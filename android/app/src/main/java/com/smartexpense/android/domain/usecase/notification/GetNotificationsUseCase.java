package com.smartexpense.android.domain.usecase.notification;

import com.smartexpense.android.data.remote.dto.response.NotificationResponseDto;
import com.smartexpense.android.domain.repository.NotificationRepository;
import com.smartexpense.android.domain.repository.ResultCallback;

import java.util.List;

public class GetNotificationsUseCase {
    private final NotificationRepository repository;

    public GetNotificationsUseCase(NotificationRepository repository) {
        this.repository = repository;
    }

    public void execute(ResultCallback<List<NotificationResponseDto>> callback) {
        repository.getNotifications(callback);
    }
}
