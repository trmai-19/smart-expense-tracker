package com.smartexpense.android.presentation.profile;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.smartexpense.android.R;
import com.smartexpense.android.databinding.BottomSheetEditProfileBinding;
import com.smartexpense.android.presentation.util.ThemeManager;
import com.smartexpense.android.presentation.util.UserManager;

public class EditProfileBottomSheet extends BottomSheetDialogFragment {

    public interface OnProfileUpdatedListener {
        void onProfileUpdated();
    }

    private BottomSheetEditProfileBinding binding;
    private OnProfileUpdatedListener listener;
    private String selectedEmoji = "⭐";

    public static EditProfileBottomSheet newInstance() {
        return new EditProfileBottomSheet();
    }

    public void setOnProfileUpdatedListener(OnProfileUpdatedListener listener) {
        this.listener = listener;
    }

    @Override
    public int getTheme() {
        return R.style.ThemeOverlay_SET_BottomSheetDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetEditProfileBinding.inflate(inflater, container, false);
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

        applyTheme();
        loadCurrentData();
        setupEmojiPickers();
        setupActionButtons();
    }

    private void applyTheme() {
        int accentColor = ThemeManager.getAccentColorInt(requireContext());
        binding.ivEditHeaderIcon.setImageTintList(ColorStateList.valueOf(accentColor));
        binding.ivPreviewAvatarBg.setBackgroundTintList(ColorStateList.valueOf(accentColor));
        binding.btnSaveProfile.setBackgroundTintList(ColorStateList.valueOf(accentColor));
    }

    private void loadCurrentData() {
        String currentName = UserManager.getUserName(requireContext());
        String currentEmail = UserManager.getUserEmail(requireContext());
        String currentPhone = UserManager.getUserPhone(requireContext());
        selectedEmoji = UserManager.getAvatarEmoji(requireContext());
        long currentBudget = UserManager.getMonthlyBudget(requireContext());

        binding.etEditName.setText(currentName);
        binding.etEditEmail.setText(currentEmail);
        binding.etEditPhone.setText(currentPhone);
        binding.tvPreviewAvatarEmoji.setText(selectedEmoji);
        binding.etEditBudget.setText(String.valueOf(currentBudget));
    }

    private void setupEmojiPickers() {
        TextView[] emojiViews = new TextView[] {
                binding.emojiStar,
                binding.emojiCrown,
                binding.emojiRocket,
                binding.emojiCat,
                binding.emojiFire,
                binding.emojiTarget,
                binding.emojiMoney,
                binding.emojiFood
        };

        for (TextView tv : emojiViews) {
            tv.setOnClickListener(v -> {
                selectedEmoji = tv.getText().toString();
                binding.tvPreviewAvatarEmoji.setText(selectedEmoji);
            });
        }
    }

    private void setupActionButtons() {
        binding.btnCancelEdit.setOnClickListener(v -> dismiss());

        binding.btnSaveProfile.setOnClickListener(v -> {
            String newName = binding.etEditName.getText() != null ? binding.etEditName.getText().toString().trim() : "";
            String newEmail = binding.etEditEmail.getText() != null ? binding.etEditEmail.getText().toString().trim() : "";
            String newPhone = binding.etEditPhone.getText() != null ? binding.etEditPhone.getText().toString().trim() : "";
            String budgetStr = binding.etEditBudget.getText() != null ? binding.etEditBudget.getText().toString().trim() : "";

            if (TextUtils.isEmpty(newName)) {
                binding.tilEditName.setError("Vui lòng nhập tên hiển thị");
                return;
            } else {
                binding.tilEditName.setError(null);
            }

            long newBudget = 5000000L;
            if (!TextUtils.isEmpty(budgetStr)) {
                try {
                    newBudget = Long.parseLong(budgetStr);
                } catch (NumberFormatException ignored) {}
            }

            // Save to persistence
            UserManager.setUserName(requireContext(), newName);
            UserManager.setUserEmail(requireContext(), newEmail);
            UserManager.setUserPhone(requireContext(), newPhone);
            UserManager.setAvatarEmoji(requireContext(), selectedEmoji);
            UserManager.setMonthlyBudget(requireContext(), newBudget);

            Toast.makeText(requireContext(), "Đã lưu thông tin hồ sơ!", Toast.LENGTH_SHORT).show();

            if (listener != null) {
                listener.onProfileUpdated();
            }

            dismiss();
        });
    }
}
