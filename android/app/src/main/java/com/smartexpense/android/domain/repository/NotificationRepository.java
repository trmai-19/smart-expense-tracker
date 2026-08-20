package com.smartexpense.android.domain.repository;

import com.smartexpense.android.data.remote.dto.response.NotificationResponseDto;

import java.util.List;

public interface NotificationRepository {
    void getNotifications(ResultCallback<List<NotificationResponseDto>> callback);
    void markAsRead(String id, ResultCallback<Void> callback);
    void markAllAsRead(ResultCallback<Void> callback);
}
