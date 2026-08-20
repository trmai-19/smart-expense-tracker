package com.smartexpense.api.application.port.in;

import com.smartexpense.api.application.dto.request.LoginRequestDto;
import com.smartexpense.api.application.dto.request.RegisterRequestDto;
import com.smartexpense.api.application.dto.response.AuthResponseDto;

public interface AuthUseCase {
    AuthResponseDto login(LoginRequestDto request);
    AuthResponseDto register(RegisterRequestDto request);
}
