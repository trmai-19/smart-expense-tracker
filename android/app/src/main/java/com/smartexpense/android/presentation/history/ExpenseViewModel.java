package com.smartexpense.android.presentation.history;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.smartexpense.android.data.remote.dto.response.ExpenseResponseDto;
import com.smartexpense.android.domain.repository.ResultCallback;
import com.smartexpense.android.domain.usecase.expense.CreateExpenseUseCase;
import com.smartexpense.android.domain.usecase.expense.GetExpensesUseCase;

import java.util.List;

public class ExpenseViewModel extends ViewModel {
    private final GetExpensesUseCase getExpensesUseCase;
    private final CreateExpenseUseCase createExpenseUseCase;

    private final MutableLiveData<List<ExpenseResponseDto>> expenses = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> createSuccess = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public ExpenseViewModel(GetExpensesUseCase getExpensesUseCase, CreateExpenseUseCase createExpenseUseCase) {
        this.getExpensesUseCase = getExpensesUseCase;
        this.createExpenseUseCase = createExpenseUseCase;
    }

    public LiveData<List<ExpenseResponseDto>> getExpenses() { return expenses; }
    public LiveData<String> getError() { return error; }
    public LiveData<Boolean> getCreateSuccess() { return createSuccess; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }

    public void fetchExpenses() {
        isLoading.setValue(true);
        getExpensesUseCase.execute(new ResultCallback<List<ExpenseResponseDto>>() {
            @Override
            public void onSuccess(List<ExpenseResponseDto> result) {
                isLoading.setValue(false);
                expenses.setValue(result);
            }

            @Override
            public void onError(String err) {
                isLoading.setValue(false);
                error.setValue(err);
            }
        });
    }

    public void createExpense(double amount, String category, String photoUrl, String caption, String expenseDate) {
        isLoading.setValue(true);
        createExpenseUseCase.execute(amount, category, photoUrl, caption, expenseDate, new ResultCallback<ExpenseResponseDto>() {
            @Override
            public void onSuccess(ExpenseResponseDto result) {
                isLoading.setValue(false);
                createSuccess.setValue(true);
                // Refresh list automatically
                fetchExpenses();
            }

            @Override
            public void onError(String err) {
                isLoading.setValue(false);
                error.setValue(err);
            }
        });
    }
}
