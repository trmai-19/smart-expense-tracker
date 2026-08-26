package com.smartexpense.android.presentation.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartexpense.android.data.remote.dto.response.NotificationResponseDto
import com.smartexpense.android.domain.usecase.notification.GetNotificationsUseCase
import com.smartexpense.android.domain.usecase.notification.MarkAllNotificationsReadUseCase
import com.smartexpense.android.domain.usecase.notification.MarkNotificationReadUseCase
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val markNotificationReadUseCase: MarkNotificationReadUseCase,
    private val markAllNotificationsReadUseCase: MarkAllNotificationsReadUseCase
) : ViewModel() {

    private val _notifications = MutableLiveData<List<NotificationResponseDto>>()
    val notifications: LiveData<List<NotificationResponseDto>> get() = _notifications

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchNotifications() {
        viewModelScope.launch {
            val result = getNotificationsUseCase.execute()
            result.onSuccess {
                _notifications.value = it
            }.onFailure {
                _error.value = it.message ?: "Lỗi không xác định"
            }
        }
    }

    fun markAsRead(id: String) {
        viewModelScope.launch {
            val result = markNotificationReadUseCase.execute(id)
            result.onSuccess {
                fetchNotifications()
            }.onFailure {
                _error.value = it.message ?: "Lỗi không xác định"
            }
        }
    }

    fun markAllAsRead() {
        viewModelScope.launch {
            val result = markAllNotificationsReadUseCase.execute()
            result.onSuccess {
                fetchNotifications()
            }.onFailure {
                _error.value = it.message ?: "Lỗi không xác định"
            }
        }
    }
}
