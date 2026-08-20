package com.smartexpense.android.di;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.smartexpense.android.data.remote.RetrofitClient;
import com.smartexpense.android.data.remote.api.AuthApi;
import com.smartexpense.android.data.remote.api.ExpenseApi;
import com.smartexpense.android.data.remote.api.NotificationApi;
import com.smartexpense.android.data.remote.api.UserApi;
import com.smartexpense.android.data.repository.AuthRepositoryImpl;
import com.smartexpense.android.data.repository.ExpenseRepositoryImpl;
import com.smartexpense.android.data.repository.NotificationRepositoryImpl;
import com.smartexpense.android.data.repository.UserRepositoryImpl;
import com.smartexpense.android.domain.usecase.auth.LoginUseCase;
import com.smartexpense.android.domain.usecase.auth.RegisterUseCase;
import com.smartexpense.android.domain.usecase.expense.CreateExpenseUseCase;
import com.smartexpense.android.domain.usecase.expense.GetExpensesUseCase;
import com.smartexpense.android.domain.usecase.notification.GetNotificationsUseCase;
import com.smartexpense.android.domain.usecase.notification.MarkAllNotificationsReadUseCase;
import com.smartexpense.android.domain.usecase.notification.MarkNotificationReadUseCase;
import com.smartexpense.android.domain.usecase.user.GetMeUseCase;
import com.smartexpense.android.domain.usecase.user.UpdateMeUseCase;
import com.smartexpense.android.presentation.auth.AuthViewModel;
import com.smartexpense.android.presentation.history.ExpenseViewModel;
import com.smartexpense.android.presentation.notification.NotificationViewModel;
import com.smartexpense.android.presentation.profile.ProfileViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private static ViewModelFactory instance;

    public static ViewModelFactory getInstance() {
        if (instance == null) {
            instance = new ViewModelFactory();
        }
        return instance;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AuthViewModel.class)) {
            AuthApi api = RetrofitClient.getClient().create(AuthApi.class);
            AuthRepositoryImpl repo = new AuthRepositoryImpl(api);
            return (T) new AuthViewModel(new LoginUseCase(repo), new RegisterUseCase(repo));
        }
        if (modelClass.isAssignableFrom(ProfileViewModel.class)) {
            UserApi api = RetrofitClient.getClient().create(UserApi.class);
            UserRepositoryImpl repo = new UserRepositoryImpl(api);
            return (T) new ProfileViewModel(new GetMeUseCase(repo), new UpdateMeUseCase(repo));
        }
        if (modelClass.isAssignableFrom(ExpenseViewModel.class)) {
            ExpenseApi api = RetrofitClient.getClient().create(ExpenseApi.class);
            ExpenseRepositoryImpl repo = new ExpenseRepositoryImpl(api);
            return (T) new ExpenseViewModel(new GetExpensesUseCase(repo), new CreateExpenseUseCase(repo));
        }
        if (modelClass.isAssignableFrom(NotificationViewModel.class)) {
            NotificationApi api = RetrofitClient.getClient().create(NotificationApi.class);
            NotificationRepositoryImpl repo = new NotificationRepositoryImpl(api);
            return (T) new NotificationViewModel(
                    new GetNotificationsUseCase(repo),
                    new MarkNotificationReadUseCase(repo),
                    new MarkAllNotificationsReadUseCase(repo)
            );
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
