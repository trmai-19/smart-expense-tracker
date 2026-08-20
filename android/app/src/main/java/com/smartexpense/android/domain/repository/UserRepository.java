package com.smartexpense.android.domain.repository;

import com.smartexpense.android.data.remote.dto.request.UpdateProfileRequestDto;
import com.smartexpense.android.data.remote.dto.response.UserProfileResponseDto;

public interface UserRepository {
    void getMe(ResultCallback<UserProfileResponseDto> callback);
    void updateMe(UpdateProfileRequestDto request, ResultCallback<UserProfileResponseDto> callback);
}
