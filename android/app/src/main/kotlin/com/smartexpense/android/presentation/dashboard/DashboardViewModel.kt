package com.smartexpense.android.presentation.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartexpense.android.data.remote.dto.response.StatisticsResponseDto
import com.smartexpense.android.domain.usecase.expense.GetStatisticsUseCase
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val getStatisticsUseCase: GetStatisticsUseCase
) : ViewModel() {

    private val _statistics = MutableLiveData<StatisticsResponseDto>()
    val statistics: LiveData<StatisticsResponseDto> get() = _statistics

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun fetchStatistics(period: String, fromDate: String? = null, toDate: String? = null) {
        _isLoading.value = true
        viewModelScope.launch {
            val result = getStatisticsUseCase.execute(period, fromDate, toDate)
            _isLoading.value = false
            result.onSuccess {
                _statistics.value = it
            }.onFailure {
                _error.value = it.message ?: "Lỗi tải thống kê"
            }
        }
    }
}
