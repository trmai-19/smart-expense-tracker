package com.smartexpense.android.presentation.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.smartexpense.android.data.remote.dto.response.UserProfileResponseDto;
import com.smartexpense.android.domain.repository.ResultCallback;
import com.smartexpense.android.domain.usecase.user.GetMeUseCase;
import com.smartexpense.android.domain.usecase.user.UpdateMeUseCase;

public class ProfileViewModel extends ViewModel {
    private final GetMeUseCase getMeUseCase;
    private final UpdateMeUseCase updateMeUseCase;

    private final MutableLiveData<UserProfileResponseDto> userProfile = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> updateSuccess = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public ProfileViewModel(GetMeUseCase getMeUseCase, UpdateMeUseCase updateMeUseCase) {
        this.getMeUseCase = getMeUseCase;
        this.updateMeUseCase = updateMeUseCase;
    }

    public LiveData<UserProfileResponseDto> getUserProfile() { return userProfile; }
    public LiveData<String> getError() { return error; }
    public LiveData<Boolean> getUpdateSuccess() { return updateSuccess; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }

    public void fetchProfile() {
        isLoading.setValue(true);
        getMeUseCase.execute(new ResultCallback<UserProfileResponseDto>() {
            @Override
            public void onSuccess(UserProfileResponseDto result) {
                isLoading.setValue(false);
                userProfile.setValue(result);
            }

            @Override
            public void onError(String err) {
                isLoading.setValue(false);
                error.setValue(err);
            }
        });
    }

    public void updateProfile(String displayName, String avatarUrl, Double monthlyBudget, String themeColor) {
        isLoading.setValue(true);
        updateMeUseCase.execute(displayName, avatarUrl, monthlyBudget, themeColor, new ResultCallback<UserProfileResponseDto>() {
            @Override
            public void onSuccess(UserProfileResponseDto result) {
                isLoading.setValue(false);
                userProfile.setValue(result);
                updateSuccess.setValue(true);
            }

            @Override
            public void onError(String err) {
                isLoading.setValue(false);
                error.setValue(err);
            }
        });
    }
}
