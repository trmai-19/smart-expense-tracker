package com.smartexpense.android.data.remote.api;

import com.smartexpense.android.data.remote.dto.request.LoginRequestDto;
import com.smartexpense.android.data.remote.dto.request.RegisterRequestDto;
import com.smartexpense.android.data.remote.dto.response.AuthResponseDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {
    @POST("api/auth/login")
    Call<AuthResponseDto> login(@Body LoginRequestDto request);

    @POST("api/auth/register")
    Call<AuthResponseDto> register(@Body RegisterRequestDto request);
}
