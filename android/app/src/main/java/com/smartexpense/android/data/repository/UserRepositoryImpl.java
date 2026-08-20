package com.smartexpense.android.data.repository;

import com.smartexpense.android.data.remote.api.UserApi;
import com.smartexpense.android.data.remote.dto.request.UpdateProfileRequestDto;
import com.smartexpense.android.data.remote.dto.response.UserProfileResponseDto;
import com.smartexpense.android.domain.repository.ResultCallback;
import com.smartexpense.android.domain.repository.UserRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepositoryImpl implements UserRepository {

    private final UserApi api;

    public UserRepositoryImpl(UserApi api) {
        this.api = api;
    }

    @Override
    public void getMe(ResultCallback<UserProfileResponseDto> callback) {
        api.getMe().enqueue(new Callback<UserProfileResponseDto>() {
            @Override
            public void onResponse(Call<UserProfileResponseDto> call, Response<UserProfileResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Lỗi tải thông tin: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponseDto> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    @Override
    public void updateMe(UpdateProfileRequestDto request, ResultCallback<UserProfileResponseDto> callback) {
        api.updateMe(request).enqueue(new Callback<UserProfileResponseDto>() {
            @Override
            public void onResponse(Call<UserProfileResponseDto> call, Response<UserProfileResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Lỗi cập nhật thông tin: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponseDto> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
}
