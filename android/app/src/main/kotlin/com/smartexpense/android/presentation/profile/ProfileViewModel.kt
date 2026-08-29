package com.smartexpense.android.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartexpense.android.data.remote.dto.response.UserProfileResponseDto
import com.smartexpense.android.domain.usecase.user.GetMeUseCase
import com.smartexpense.android.domain.usecase.user.UpdateMeUseCase
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getMeUseCase: GetMeUseCase,
    private val updateMeUseCase: UpdateMeUseCase
) : ViewModel() {

    private val _userProfile = MutableLiveData<UserProfileResponseDto>()
    val userProfile: LiveData<UserProfileResponseDto> get() = _userProfile

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _updateSuccess = MutableLiveData<Boolean>()
    val updateSuccess: LiveData<Boolean> get() = _updateSuccess

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun fetchProfile() {
        _isLoading.value = true
        viewModelScope.launch {
            val result = getMeUseCase.execute()
            _isLoading.value = false
            result.onSuccess {
                _userProfile.value = it
            }.onFailure {
                _error.value = it.message ?: "Lỗi không xác định"
            }
        }
    }

    fun updateProfile(displayName: String?, avatarUrl: String?, monthlyBudget: Double?, themeColor: String?) {
        _isLoading.value = true
        viewModelScope.launch {
            val result = updateMeUseCase.execute(displayName, avatarUrl, monthlyBudget, themeColor)
            _isLoading.value = false
            result.onSuccess {
                _userProfile.value = it
                _updateSuccess.value = true
            }.onFailure {
                _error.value = it.message ?: "Lỗi không xác định"
            }
        }
    }
}
