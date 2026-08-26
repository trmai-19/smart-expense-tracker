package com.smartexpense.android.presentation.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartexpense.android.data.remote.dto.response.ExpenseResponseDto
import com.smartexpense.android.domain.usecase.expense.CreateExpenseUseCase
import com.smartexpense.android.domain.usecase.expense.GetExpensesUseCase
import kotlinx.coroutines.launch

class ExpenseViewModel(
    private val getExpensesUseCase: GetExpensesUseCase,
    private val createExpenseUseCase: CreateExpenseUseCase
) : ViewModel() {

    private val _expenses = MutableLiveData<List<ExpenseResponseDto>>()
    val expenses: LiveData<List<ExpenseResponseDto>?> get() = _expenses

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _createSuccess = MutableLiveData<Boolean>()
    val createSuccess: LiveData<Boolean> get() = _createSuccess

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun fetchExpenses() {
        _isLoading.value = true
        viewModelScope.launch {
            val result = getExpensesUseCase.execute()
            _isLoading.value = false
            result.onSuccess {
                _expenses.value = it
            }.onFailure {
                _error.value = it.message ?: "Lỗi không xác định"
            }
        }
    }

    fun createExpense(amount: Double, category: String, photoUrl: String, caption: String, expenseDate: String) {
        _isLoading.value = true
        viewModelScope.launch {
            val result = createExpenseUseCase.execute(amount, category, photoUrl, caption, expenseDate)
            _isLoading.value = false
            result.onSuccess {
                _createSuccess.value = true
                fetchExpenses()
            }.onFailure {
                _error.value = it.message ?: "Lỗi không xác định"
            }
        }
    }
}
