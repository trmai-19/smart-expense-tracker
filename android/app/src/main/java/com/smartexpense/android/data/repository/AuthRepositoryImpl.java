package com.smartexpense.android.data.repository;

import com.smartexpense.android.data.remote.api.AuthApi;
import com.smartexpense.android.data.remote.dto.request.LoginRequestDto;
import com.smartexpense.android.data.remote.dto.request.RegisterRequestDto;
import com.smartexpense.android.data.remote.dto.response.AuthResponseDto;
import com.smartexpense.android.domain.repository.AuthRepository;
import com.smartexpense.android.domain.repository.ResultCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepositoryImpl implements AuthRepository {

    private final AuthApi api;

    public AuthRepositoryImpl(AuthApi api) {
        this.api = api;
    }

    @Override
    public void login(LoginRequestDto request, ResultCallback<AuthResponseDto> callback) {
        api.login(request).enqueue(new Callback<AuthResponseDto>() {
            @Override
            public void onResponse(Call<AuthResponseDto> call, Response<AuthResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Đăng nhập thất bại: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<AuthResponseDto> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    @Override
    public void register(RegisterRequestDto request, ResultCallback<AuthResponseDto> callback) {
        api.register(request).enqueue(new Callback<AuthResponseDto>() {
            @Override
            public void onResponse(Call<AuthResponseDto> call, Response<AuthResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Đăng ký thất bại: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<AuthResponseDto> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
}
