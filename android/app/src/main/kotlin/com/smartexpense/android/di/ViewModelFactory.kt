package com.smartexpense.android.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.smartexpense.android.data.remote.RetrofitClient
import com.smartexpense.android.data.remote.api.AuthApi
import com.smartexpense.android.data.remote.api.ChatApi
import com.smartexpense.android.data.remote.api.ExpenseApi
import com.smartexpense.android.data.remote.api.NotificationApi
import com.smartexpense.android.data.remote.api.UserApi
import com.smartexpense.android.data.repository.AuthRepositoryImpl
import com.smartexpense.android.data.repository.ChatRepositoryImpl
import com.smartexpense.android.data.repository.ExpenseRepositoryImpl
import com.smartexpense.android.data.repository.NotificationRepositoryImpl
import com.smartexpense.android.data.repository.UserRepositoryImpl
import com.smartexpense.android.domain.usecase.auth.LoginUseCase
import com.smartexpense.android.domain.usecase.auth.RegisterUseCase
import com.smartexpense.android.domain.usecase.chat.SendChatMessageUseCase
import com.smartexpense.android.domain.usecase.chat.GetChatHistoryUseCase
import com.smartexpense.android.domain.usecase.expense.AnalyzeExpenseUseCase
import com.smartexpense.android.domain.usecase.expense.CreateExpenseUseCase
import com.smartexpense.android.domain.usecase.expense.GetExpensesUseCase
import com.smartexpense.android.domain.usecase.expense.GetStatisticsUseCase
import com.smartexpense.android.domain.usecase.notification.GetNotificationsUseCase
import com.smartexpense.android.domain.usecase.notification.MarkAllNotificationsReadUseCase
import com.smartexpense.android.domain.usecase.notification.MarkNotificationReadUseCase
import com.smartexpense.android.domain.usecase.user.GetMeUseCase
import com.smartexpense.android.domain.usecase.user.UpdateMeUseCase
import com.smartexpense.android.presentation.auth.AuthViewModel
import com.smartexpense.android.presentation.chat.ChatViewModel
import com.smartexpense.android.presentation.history.ExpenseViewModel
import com.smartexpense.android.presentation.notification.NotificationViewModel
import com.smartexpense.android.presentation.profile.ProfileViewModel
import com.smartexpense.android.presentation.dashboard.DashboardViewModel

class ViewModelFactory private constructor() : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                val api = RetrofitClient.getClient().create(AuthApi::class.java)
                val repo = AuthRepositoryImpl(api)
                AuthViewModel(LoginUseCase(repo), RegisterUseCase(repo)) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                val api = RetrofitClient.getClient().create(UserApi::class.java)
                val repo = UserRepositoryImpl(api)
                ProfileViewModel(GetMeUseCase(repo), UpdateMeUseCase(repo)) as T
            }
            modelClass.isAssignableFrom(ExpenseViewModel::class.java) -> {
                val api = RetrofitClient.getClient().create(ExpenseApi::class.java)
                val repo = ExpenseRepositoryImpl(api)
                ExpenseViewModel(GetExpensesUseCase(repo), CreateExpenseUseCase(repo), AnalyzeExpenseUseCase(repo), repo) as T
            }
            modelClass.isAssignableFrom(DashboardViewModel::class.java) -> {
                val api = RetrofitClient.getClient().create(ExpenseApi::class.java)
                val repo = ExpenseRepositoryImpl(api)
                DashboardViewModel(GetStatisticsUseCase(repo)) as T
            }
            modelClass.isAssignableFrom(NotificationViewModel::class.java) -> {
                val api = RetrofitClient.getClient().create(NotificationApi::class.java)
                val repo = NotificationRepositoryImpl(api)
                NotificationViewModel(
                    GetNotificationsUseCase(repo),
                    MarkNotificationReadUseCase(repo),
                    MarkAllNotificationsReadUseCase(repo)
                ) as T
            }
            modelClass.isAssignableFrom(ChatViewModel::class.java) -> {
                val api = RetrofitClient.getClient().create(ChatApi::class.java)
                val repo = ChatRepositoryImpl(api)
                ChatViewModel(SendChatMessageUseCase(repo), GetChatHistoryUseCase(repo)) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(): ViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: ViewModelFactory().also { instance = it }
            }
        }
    }
}
