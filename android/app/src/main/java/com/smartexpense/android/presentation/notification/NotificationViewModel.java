package com.smartexpense.android.presentation.notification;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.smartexpense.android.data.remote.dto.response.NotificationResponseDto;
import com.smartexpense.android.domain.repository.ResultCallback;
import com.smartexpense.android.domain.usecase.notification.GetNotificationsUseCase;
import com.smartexpense.android.domain.usecase.notification.MarkAllNotificationsReadUseCase;
import com.smartexpense.android.domain.usecase.notification.MarkNotificationReadUseCase;

import java.util.List;

public class NotificationViewModel extends ViewModel {
    private final GetNotificationsUseCase getNotificationsUseCase;
    private final MarkNotificationReadUseCase markNotificationReadUseCase;
    private final MarkAllNotificationsReadUseCase markAllNotificationsReadUseCase;

    private final MutableLiveData<List<NotificationResponseDto>> notifications = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public NotificationViewModel(GetNotificationsUseCase getNotificationsUseCase,
                                 MarkNotificationReadUseCase markNotificationReadUseCase,
                                 MarkAllNotificationsReadUseCase markAllNotificationsReadUseCase) {
        this.getNotificationsUseCase = getNotificationsUseCase;
        this.markNotificationReadUseCase = markNotificationReadUseCase;
        this.markAllNotificationsReadUseCase = markAllNotificationsReadUseCase;
    }

    public LiveData<List<NotificationResponseDto>> getNotifications() { return notifications; }
    public LiveData<String> getError() { return error; }

    public void fetchNotifications() {
        getNotificationsUseCase.execute(new ResultCallback<List<NotificationResponseDto>>() {
            @Override
            public void onSuccess(List<NotificationResponseDto> result) {
                notifications.setValue(result);
            }

            @Override
            public void onError(String err) {
                error.setValue(err);
            }
        });
    }

    public void markAsRead(String id) {
        markNotificationReadUseCase.execute(id, new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                fetchNotifications(); // Refresh
            }

            @Override
            public void onError(String err) {
                error.setValue(err);
            }
        });
    }

    public void markAllAsRead() {
        markAllNotificationsReadUseCase.execute(new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                fetchNotifications(); // Refresh
            }

            @Override
            public void onError(String err) {
                error.setValue(err);
            }
        });
    }
}
