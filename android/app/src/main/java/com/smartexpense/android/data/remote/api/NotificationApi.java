package com.smartexpense.android.data.remote.api;

import com.smartexpense.android.data.remote.dto.response.NotificationResponseDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface NotificationApi {
    @GET("api/notifications")
    Call<List<NotificationResponseDto>> getNotifications();

    @PATCH("api/notifications/{id}/read")
    Call<Void> markAsRead(@Path("id") String id);

    @PATCH("api/notifications/read-all")
    Call<Void> markAllAsRead();
}
