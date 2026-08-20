package com.smartexpense.android.presentation.auth.register;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.smartexpense.android.data.remote.RetrofitClient;
import com.smartexpense.android.databinding.ActivityRegisterBinding;
import com.smartexpense.android.di.ViewModelFactory;
import com.smartexpense.android.presentation.auth.AuthViewModel;
import com.smartexpense.android.presentation.main.MainActivity;
import com.smartexpense.android.presentation.util.ThemeManager;
import com.smartexpense.android.presentation.util.UserManager;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private AuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(AuthViewModel.class);

        applyAccentColor();
        setupListeners();
        observeViewModel();
    }

    private void applyAccentColor() {
        int accentColor = ThemeManager.getAccentColorInt(this);
        binding.btnRegister.setBackgroundTintList(ColorStateList.valueOf(accentColor));
        binding.tvGoLogin.setTextColor(accentColor);
    }

    private void setupListeners() {
        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnRegister.setOnClickListener(v -> {
            String name = binding.etDisplayName.getText() != null ? binding.etDisplayName.getText().toString().trim() : "";
            String username = binding.etUsername.getText() != null ? binding.etUsername.getText().toString().trim() : "";
            String password = binding.etPassword.getText() != null ? binding.etPassword.getText().toString().trim() : "";
            String confirmPassword = binding.etConfirmPassword.getText() != null ? binding.etConfirmPassword.getText().toString().trim() : "";

            if (name.isEmpty()) {
                binding.tilDisplayName.setError("Vui lòng nhập tên hiển thị");
                return;
            } else {
                binding.tilDisplayName.setError(null);
            }

            if (username.isEmpty()) {
                binding.tilUsername.setError("Vui lòng nhập email");
                return;
            } else {
                binding.tilUsername.setError(null);
            }

            if (password.length() < 6) {
                binding.tilPassword.setError("Mật khẩu phải từ 6 ký tự trở lên");
                return;
            } else {
                binding.tilPassword.setError(null);
            }

            if (!password.equals(confirmPassword)) {
                binding.tilConfirmPassword.setError("Mật khẩu xác nhận không khớp");
                return;
            } else {
                binding.tilConfirmPassword.setError(null);
            }

            viewModel.register(username, password, name);
        });

        binding.tvGoLogin.setOnClickListener(v -> finish());
    }

    private void observeViewModel() {
        viewModel.getAuthSuccess().observe(this, response -> {
            if (response != null && response.getToken() != null) {
                // Save Token
                RetrofitClient.getTokenManager().saveToken(response.getToken());

                // Save Display Name
                if (response.getUserProfile() != null && response.getUserProfile().getDisplayName() != null) {
                    UserManager.setUserName(this, response.getUserProfile().getDisplayName());
                }

                Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
                binding.btnRegister.setEnabled(!isLoading);
                binding.btnRegister.setText(isLoading ? "Đang đăng ký..." : "Đăng ký");
            }
        });
    }
}
