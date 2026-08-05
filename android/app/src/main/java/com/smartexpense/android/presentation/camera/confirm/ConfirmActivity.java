package com.smartexpense.android.presentation.camera.confirm;

import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.smartexpense.android.databinding.ActivityConfirmBinding;
import com.smartexpense.android.presentation.util.ThemeManager;

public class ConfirmActivity extends AppCompatActivity {

    private ActivityConfirmBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfirmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        applyAccentColor();
        loadCapturedImage();
        setupListeners();
    }

    private void applyAccentColor() {
        int accentColor = ThemeManager.getAccentColorInt(this);
        binding.btnPost.setBackgroundTintList(ColorStateList.valueOf(accentColor));
    }

    private void loadCapturedImage() {
        String imageUriString = getIntent().getStringExtra("image_uri");
        if (imageUriString != null) {
            try {
                Uri imageUri = Uri.parse(imageUriString);
                binding.ivCapturedPhoto.setImageURI(imageUri);
            } catch (Exception e) {
                // Keep default placeholder
            }
        }
    }

    private void setupListeners() {
        binding.btnClose.setOnClickListener(v -> finish());
        binding.btnRetake.setOnClickListener(v -> finish());

        binding.btnPost.setOnClickListener(v -> {
            String caption = binding.etCaption.getText() != null
                    ? binding.etCaption.getText().toString().trim()
                    : "";

            String message = caption.isEmpty()
                    ? "Đã ghi nhận chi tiêu thành công!"
                    : "Đã đăng chi tiêu: \"" + caption + "\"";

            Toast.makeText(ConfirmActivity.this, message, Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
