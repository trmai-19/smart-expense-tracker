package com.smartexpense.android.domain.repository;

import com.smartexpense.android.data.remote.dto.request.LoginRequestDto;
import com.smartexpense.android.data.remote.dto.request.RegisterRequestDto;
import com.smartexpense.android.data.remote.dto.response.AuthResponseDto;

public interface AuthRepository {
    void login(LoginRequestDto request, ResultCallback<AuthResponseDto> callback);
    void register(RegisterRequestDto request, ResultCallback<AuthResponseDto> callback);
}
