package com.smartexpense.android.presentation.auth.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.smartexpense.android.data.remote.RetrofitClient;
import com.smartexpense.android.databinding.ActivityLoginBinding;
import com.smartexpense.android.di.ViewModelFactory;
import com.smartexpense.android.presentation.auth.AuthViewModel;
import com.smartexpense.android.presentation.auth.register.RegisterActivity;
import com.smartexpense.android.presentation.main.MainActivity;
import com.smartexpense.android.presentation.util.ThemeManager;
import com.smartexpense.android.presentation.util.UserManager;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private AuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(AuthViewModel.class);

        applyAccentColor();
        setupListeners();
        observeViewModel();
    }

    private void applyAccentColor() {
        int accentColor = ThemeManager.getAccentColorInt(this);
        binding.btnLogin.setBackgroundTintList(android.content.res.ColorStateList.valueOf(accentColor));
        binding.tvGoRegister.setTextColor(accentColor);
        binding.ivLogo.setImageTintList(android.content.res.ColorStateList.valueOf(accentColor));
    }

    private void setupListeners() {
        binding.btnLogin.setOnClickListener(v -> {
            String username = binding.etUsername.getText() != null ? binding.etUsername.getText().toString().trim() : "";
            String password = binding.etPassword.getText() != null ? binding.etPassword.getText().toString().trim() : "";

            if (username.isEmpty()) {
                binding.tilUsername.setError("Vui lòng nhập tên đăng nhập hoặc email");
                return;
            } else {
                binding.tilUsername.setError(null);
            }

            if (password.isEmpty()) {
                binding.tilPassword.setError("Vui lòng nhập mật khẩu");
                return;
            } else {
                binding.tilPassword.setError(null);
            }

            viewModel.login(username, password);
        });

        binding.tvGoRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void observeViewModel() {
        viewModel.getAuthSuccess().observe(this, response -> {
            if (response != null && response.getToken() != null) {
                // Save Token
                RetrofitClient.getTokenManager().saveToken(response.getToken());

                // Save Display Name to Local prefs
                if (response.getUserProfile() != null && response.getUserProfile().getDisplayName() != null) {
                    UserManager.setUserName(this, response.getUserProfile().getDisplayName());
                }

                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        viewModel.getAuthError().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading != null) {
                binding.btnLogin.setEnabled(!isLoading);
                binding.btnLogin.setText(isLoading ? "Đang đăng nhập..." : "Đăng nhập");
            }
        });
    }
}
