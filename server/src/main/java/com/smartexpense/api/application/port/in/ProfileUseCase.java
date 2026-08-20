package com.smartexpense.api.application.port.in;

import com.smartexpense.api.application.dto.request.UpdateProfileRequestDto;
import com.smartexpense.api.application.dto.response.UserProfileResponseDto;

public interface ProfileUseCase {
    UserProfileResponseDto getProfile(String email);
    UserProfileResponseDto updateProfile(String email, UpdateProfileRequestDto request);
}
