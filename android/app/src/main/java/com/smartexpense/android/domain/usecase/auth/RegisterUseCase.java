package com.smartexpense.android.domain.usecase.auth;

import com.smartexpense.android.data.remote.dto.request.RegisterRequestDto;
import com.smartexpense.android.data.remote.dto.response.AuthResponseDto;
import com.smartexpense.android.domain.repository.AuthRepository;
import com.smartexpense.android.domain.repository.ResultCallback;

public class RegisterUseCase {
    private final AuthRepository repository;

    public RegisterUseCase(AuthRepository repository) {
        this.repository = repository;
    }

    public void execute(String email, String password, String displayName, ResultCallback<AuthResponseDto> callback) {
        RegisterRequestDto request = new RegisterRequestDto(email, password, displayName);
        repository.register(request, callback);
    }
}
