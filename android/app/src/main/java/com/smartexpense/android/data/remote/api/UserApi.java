package com.smartexpense.android.data.remote.api;

import com.smartexpense.android.data.remote.dto.request.UpdateProfileRequestDto;
import com.smartexpense.android.data.remote.dto.response.UserProfileResponseDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;

public interface UserApi {
    @GET("api/users/me")
    Call<UserProfileResponseDto> getMe();

    @PUT("api/users/me")
    Call<UserProfileResponseDto> updateMe(@Body UpdateProfileRequestDto request);
}
