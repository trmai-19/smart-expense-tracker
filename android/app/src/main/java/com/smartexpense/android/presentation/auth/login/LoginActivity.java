package com.smartexpense.android.presentation.auth.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.smartexpense.android.databinding.ActivityLoginBinding;
import com.smartexpense.android.presentation.auth.register.RegisterActivity;
import com.smartexpense.android.presentation.main.MainActivity;
import com.smartexpense.android.presentation.util.ThemeManager;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        applyAccentColor();
        setupListeners();
    }

    private void applyAccentColor() {
        int accentColor = ThemeManager.getAccentColorInt(this);
        binding.btnLogin.setBackgroundTintList(android.content.res.ColorStateList.valueOf(accentColor));
        binding.tvGoRegister.setTextColor(accentColor);
        // Tint the star logo to match accent color
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

            // Demo authentication - proceed to Main camera screen
            if (!username.contains("@")) {
                com.smartexpense.android.presentation.util.UserManager.setUserName(LoginActivity.this, username);
            }
            Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });

        binding.tvGoRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
