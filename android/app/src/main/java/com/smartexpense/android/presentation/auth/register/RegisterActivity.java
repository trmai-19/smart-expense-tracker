package com.smartexpense.android.presentation.auth.register;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.smartexpense.android.databinding.ActivityRegisterBinding;
import com.smartexpense.android.presentation.main.MainActivity;
import com.smartexpense.android.presentation.util.ThemeManager;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        applyAccentColor();
        setupListeners();
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
                binding.tilUsername.setError("Vui lòng nhập tên đăng nhập hoặc email");
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

            com.smartexpense.android.presentation.util.UserManager.setUserName(RegisterActivity.this, name);
            Toast.makeText(RegisterActivity.this, "Đăng ký thành công! Chào mừng " + name, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });

        binding.tvGoLogin.setOnClickListener(v -> finish());
    }
}
