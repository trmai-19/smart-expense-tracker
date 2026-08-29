package com.smartexpense.android.data.remote.api

import com.smartexpense.android.data.remote.dto.request.LoginRequestDto
import com.smartexpense.android.data.remote.dto.request.RegisterRequestDto
import com.smartexpense.android.data.remote.dto.response.AuthResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequestDto): AuthResponseDto

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequestDto): AuthResponseDto

    @POST("api/auth/refresh")
    fun refreshToken(@Body request: com.smartexpense.android.data.remote.dto.request.RefreshTokenRequestDto): retrofit2.Call<AuthResponseDto>
}
