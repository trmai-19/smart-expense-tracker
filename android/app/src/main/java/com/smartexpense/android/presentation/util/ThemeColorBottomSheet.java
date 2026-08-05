package com.smartexpense.android.presentation.util;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.smartexpense.android.R;
import com.smartexpense.android.databinding.BottomSheetColorPaletteBinding;

public class ThemeColorBottomSheet extends BottomSheetDialogFragment {

    public interface OnColorSelectedListener {
        void onColorSelected(ThemeManager.AccentColor color);
    }

    private BottomSheetColorPaletteBinding binding;
    private OnColorSelectedListener listener;

    public static ThemeColorBottomSheet newInstance() {
        return new ThemeColorBottomSheet();
    }

    public void setOnColorSelectedListener(OnColorSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public int getTheme() {
        return R.style.ThemeOverlay_SET_BottomSheetDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetColorPaletteBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            View bottomSheet = getDialog().findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                bottomSheet.setBackgroundResource(android.R.color.transparent);
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupColorClicks();
        highlightSelectedColor();
    }

    private void setupColorClicks() {
        binding.itemColorLime.setOnClickListener(v -> selectColor(ThemeManager.AccentColor.NEON_LIME));
        binding.itemColorCyan.setOnClickListener(v -> selectColor(ThemeManager.AccentColor.NEON_CYAN));
        binding.itemColorYellow.setOnClickListener(v -> selectColor(ThemeManager.AccentColor.NEON_YELLOW));
        binding.itemColorPink.setOnClickListener(v -> selectColor(ThemeManager.AccentColor.NEON_PINK));
        binding.itemColorPurple.setOnClickListener(v -> selectColor(ThemeManager.AccentColor.NEON_PURPLE));
        binding.itemColorOrange.setOnClickListener(v -> selectColor(ThemeManager.AccentColor.NEON_ORANGE));
        binding.itemColorMint.setOnClickListener(v -> selectColor(ThemeManager.AccentColor.NEON_MINT));
        binding.itemColorCoral.setOnClickListener(v -> selectColor(ThemeManager.AccentColor.NEON_CORAL));
    }

    private void selectColor(ThemeManager.AccentColor color) {
        if (getContext() != null) {
            ThemeManager.setAccentColor(requireContext(), color);
            Toast.makeText(requireContext(), "Đã chọn màu: " + color.getDisplayName(), Toast.LENGTH_SHORT).show();
        }

        if (listener != null) {
            listener.onColorSelected(color);
        }

        dismiss();
    }

    private void highlightSelectedColor() {
        if (getContext() == null || binding == null) return;
        ThemeManager.AccentColor current = ThemeManager.getAccentColor(requireContext());

        // Hide all checkmarks
        binding.ivLimeCheck.setVisibility(View.GONE);
        binding.ivCyanCheck.setVisibility(View.GONE);
        binding.ivYellowCheck.setVisibility(View.GONE);
        binding.ivPinkCheck.setVisibility(View.GONE);
        binding.ivPurpleCheck.setVisibility(View.GONE);
        binding.ivOrangeCheck.setVisibility(View.GONE);
        binding.ivMintCheck.setVisibility(View.GONE);
        binding.ivCoralCheck.setVisibility(View.GONE);

        // Show centered white checkmark on selected color without scaling
        switch (current) {
            case NEON_LIME:
                binding.ivLimeCheck.setVisibility(View.VISIBLE);
                break;
            case NEON_CYAN:
                binding.ivCyanCheck.setVisibility(View.VISIBLE);
                break;
            case NEON_YELLOW:
                binding.ivYellowCheck.setVisibility(View.VISIBLE);
                break;
            case NEON_PINK:
                binding.ivPinkCheck.setVisibility(View.VISIBLE);
                break;
            case NEON_PURPLE:
                binding.ivPurpleCheck.setVisibility(View.VISIBLE);
                break;
            case NEON_ORANGE:
                binding.ivOrangeCheck.setVisibility(View.VISIBLE);
                break;
            case NEON_MINT:
                binding.ivMintCheck.setVisibility(View.VISIBLE);
                break;
            case NEON_CORAL:
                binding.ivCoralCheck.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
