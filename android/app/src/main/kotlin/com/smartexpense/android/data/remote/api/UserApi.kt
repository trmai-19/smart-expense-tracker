package com.smartexpense.android.data.remote.api

import com.smartexpense.android.data.remote.dto.request.UpdateProfileRequestDto
import com.smartexpense.android.data.remote.dto.response.UserProfileResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface UserApi {
    @GET("api/users/me")
    suspend fun getMe(): UserProfileResponseDto

    @PUT("api/users/me")
    suspend fun updateMe(@Body request: UpdateProfileRequestDto): UserProfileResponseDto
}
