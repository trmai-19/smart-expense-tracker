package com.smartexpense.android.presentation.camera.confirm;

import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.smartexpense.android.databinding.ActivityConfirmBinding;
import com.smartexpense.android.di.ViewModelFactory;
import com.smartexpense.android.presentation.history.ExpenseViewModel;
import com.smartexpense.android.presentation.util.ThemeManager;

import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ConfirmActivity extends AppCompatActivity {

    private ActivityConfirmBinding binding;
    private ExpenseViewModel viewModel;
    private String currentPhotoUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfirmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(ExpenseViewModel.class);

        applyAccentColor();
        loadCapturedImage();
        setupListeners();
        observeViewModel();
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
                currentPhotoUrl = imageUriString;
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

            String expenseDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(new Date());
            
            // Hardcode amount/category for demo since UI only has caption
            viewModel.createExpense(50000, "Ăn uống", currentPhotoUrl, caption, expenseDate);
        });
    }

    private void observeViewModel() {
        viewModel.getCreateSuccess().observe(this, success -> {
            if (success != null && success) {
                Toast.makeText(ConfirmActivity.this, "Đã ghi nhận chi tiêu thành công!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        viewModel.getError().observe(this, error -> {
            if (error != null) {
                Toast.makeText(ConfirmActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
