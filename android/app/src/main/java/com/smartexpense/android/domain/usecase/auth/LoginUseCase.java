package com.smartexpense.android.domain.usecase.auth;

import com.smartexpense.android.data.remote.dto.request.LoginRequestDto;
import com.smartexpense.android.data.remote.dto.response.AuthResponseDto;
import com.smartexpense.android.domain.repository.AuthRepository;
import com.smartexpense.android.domain.repository.ResultCallback;

public class LoginUseCase {
    private final AuthRepository repository;

    public LoginUseCase(AuthRepository repository) {
        this.repository = repository;
    }

    public void execute(String email, String password, ResultCallback<AuthResponseDto> callback) {
        LoginRequestDto request = new LoginRequestDto(email, password);
        repository.login(request, callback);
    }
}
