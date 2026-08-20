package com.smartexpense.android.data.repository;

import com.smartexpense.android.data.remote.api.NotificationApi;
import com.smartexpense.android.data.remote.dto.response.NotificationResponseDto;
import com.smartexpense.android.domain.repository.NotificationRepository;
import com.smartexpense.android.domain.repository.ResultCallback;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationRepositoryImpl implements NotificationRepository {

    private final NotificationApi api;

    public NotificationRepositoryImpl(NotificationApi api) {
        this.api = api;
    }

    @Override
    public void getNotifications(ResultCallback<List<NotificationResponseDto>> callback) {
        api.getNotifications().enqueue(new Callback<List<NotificationResponseDto>>() {
            @Override
            public void onResponse(Call<List<NotificationResponseDto>> call, Response<List<NotificationResponseDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Lỗi tải thông báo: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<NotificationResponseDto>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    @Override
    public void markAsRead(String id, ResultCallback<Void> callback) {
        api.markAsRead(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null);
                } else {
                    callback.onError("Lỗi đánh dấu đã đọc: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    @Override
    public void markAllAsRead(ResultCallback<Void> callback) {
        api.markAllAsRead().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null);
                } else {
                    callback.onError("Lỗi đánh dấu tất cả đã đọc: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
}
