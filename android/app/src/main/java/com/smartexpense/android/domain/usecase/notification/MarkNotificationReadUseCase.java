package com.smartexpense.android.domain.usecase.notification;

import com.smartexpense.android.domain.repository.NotificationRepository;
import com.smartexpense.android.domain.repository.ResultCallback;

public class MarkNotificationReadUseCase {
    private final NotificationRepository repository;

    public MarkNotificationReadUseCase(NotificationRepository repository) {
        this.repository = repository;
    }

    public void execute(String id, ResultCallback<Void> callback) {
        repository.markAsRead(id, callback);
    }
}
