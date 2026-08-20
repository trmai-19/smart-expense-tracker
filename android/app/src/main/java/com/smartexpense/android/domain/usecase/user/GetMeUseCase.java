package com.smartexpense.android.domain.usecase.user;

import com.smartexpense.android.data.remote.dto.response.UserProfileResponseDto;
import com.smartexpense.android.domain.repository.ResultCallback;
import com.smartexpense.android.domain.repository.UserRepository;

public class GetMeUseCase {
    private final UserRepository repository;

    public GetMeUseCase(UserRepository repository) {
        this.repository = repository;
    }

    public void execute(ResultCallback<UserProfileResponseDto> callback) {
        repository.getMe(callback);
    }
}
