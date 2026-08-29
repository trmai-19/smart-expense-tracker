package com.smartexpense.android.presentation.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartexpense.android.data.remote.dto.response.AuthResponseDto
import com.smartexpense.android.domain.usecase.auth.LoginUseCase
import com.smartexpense.android.domain.usecase.auth.RegisterUseCase
import kotlinx.coroutines.launch

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _authSuccess = MutableLiveData<AuthResponseDto>()
    val authSuccess: LiveData<AuthResponseDto> get() = _authSuccess

    private val _authError = MutableLiveData<String>()
    val authError: LiveData<String> get() = _authError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun login(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            val result = loginUseCase.execute(email, password)
            _isLoading.value = false
            result.onSuccess {
                _authSuccess.value = it
            }.onFailure {
                _authError.value = it.message ?: "Lỗi không xác định"
            }
        }
    }

    fun register(email: String, password: String, displayName: String) {
        _isLoading.value = true
        viewModelScope.launch {
            val result = registerUseCase.execute(displayName, email, password)
            _isLoading.value = false
            result.onSuccess {
                _authSuccess.value = it
            }.onFailure {
                _authError.value = it.message ?: "Lỗi không xác định"
            }
        }
    }
}
