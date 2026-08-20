package com.smartexpense.android.domain.usecase.user;

import com.smartexpense.android.data.remote.dto.request.UpdateProfileRequestDto;
import com.smartexpense.android.data.remote.dto.response.UserProfileResponseDto;
import com.smartexpense.android.domain.repository.ResultCallback;
import com.smartexpense.android.domain.repository.UserRepository;

public class UpdateMeUseCase {
    private final UserRepository repository;

    public UpdateMeUseCase(UserRepository repository) {
        this.repository = repository;
    }

    public void execute(String displayName, String avatarUrl, Double monthlyBudget, String themeColor, ResultCallback<UserProfileResponseDto> callback) {
        UpdateProfileRequestDto request = new UpdateProfileRequestDto(displayName, avatarUrl, monthlyBudget, themeColor);
        repository.updateMe(request, callback);
    }
}
