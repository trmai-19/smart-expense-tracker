package com.smartexpense.android.data.remote.api

import com.smartexpense.android.data.remote.dto.response.NotificationResponseDto
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface NotificationApi {
    @GET("api/notifications")
    suspend fun getNotifications(): List<NotificationResponseDto>

    @PATCH("api/notifications/{id}/read")
    suspend fun markAsRead(@Path("id") id: String)

    @PATCH("api/notifications/read-all")
    suspend fun markAllAsRead()
}
