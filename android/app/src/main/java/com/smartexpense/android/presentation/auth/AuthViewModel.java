package com.smartexpense.android.presentation.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.smartexpense.android.data.remote.dto.response.AuthResponseDto;
import com.smartexpense.android.domain.repository.ResultCallback;
import com.smartexpense.android.domain.usecase.auth.LoginUseCase;
import com.smartexpense.android.domain.usecase.auth.RegisterUseCase;

public class AuthViewModel extends ViewModel {
    private final LoginUseCase loginUseCase;
    private final RegisterUseCase registerUseCase;

    private final MutableLiveData<AuthResponseDto> authSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> authError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public AuthViewModel(LoginUseCase loginUseCase, RegisterUseCase registerUseCase) {
        this.loginUseCase = loginUseCase;
        this.registerUseCase = registerUseCase;
    }

    public LiveData<AuthResponseDto> getAuthSuccess() { return authSuccess; }
    public LiveData<String> getAuthError() { return authError; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }

    public void login(String email, String password) {
        isLoading.setValue(true);
        loginUseCase.execute(email, password, new ResultCallback<AuthResponseDto>() {
            @Override
            public void onSuccess(AuthResponseDto result) {
                isLoading.setValue(false);
                authSuccess.setValue(result);
            }

            @Override
            public void onError(String error) {
                isLoading.setValue(false);
                authError.setValue(error);
            }
        });
    }

    public void register(String email, String password, String displayName) {
        isLoading.setValue(true);
        registerUseCase.execute(email, password, displayName, new ResultCallback<AuthResponseDto>() {
            @Override
            public void onSuccess(AuthResponseDto result) {
                isLoading.setValue(false);
                authSuccess.setValue(result);
            }

            @Override
            public void onError(String error) {
                isLoading.setValue(false);
                authError.setValue(error);
            }
        });
    }
}
