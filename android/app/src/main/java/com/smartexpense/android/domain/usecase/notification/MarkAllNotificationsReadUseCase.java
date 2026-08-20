package com.smartexpense.android.domain.usecase.notification;

import com.smartexpense.android.domain.repository.NotificationRepository;
import com.smartexpense.android.domain.repository.ResultCallback;

public class MarkAllNotificationsReadUseCase {
    private final NotificationRepository repository;

    public MarkAllNotificationsReadUseCase(NotificationRepository repository) {
        this.repository = repository;
    }

    public void execute(ResultCallback<Void> callback) {
        repository.markAllAsRead(callback);
    }
}
