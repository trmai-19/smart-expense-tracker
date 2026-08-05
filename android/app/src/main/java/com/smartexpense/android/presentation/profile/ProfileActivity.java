package com.smartexpense.android.presentation.profile;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.smartexpense.android.databinding.ActivityProfileBinding;
import com.smartexpense.android.presentation.auth.login.LoginActivity;
import com.smartexpense.android.presentation.util.ThemeManager;
import com.smartexpense.android.presentation.util.UserManager;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadUserProfile();
        applyAccentColor();
        setupListeners();
    }

    private void loadUserProfile() {
        String name = UserManager.getUserName(this);
        String email = UserManager.getUserEmail(this);
        String emoji = UserManager.getAvatarEmoji(this);
        long budget = UserManager.getMonthlyBudget(this);
        int streak = UserManager.getStreakDays(this);
        int bills = UserManager.getTotalBillsCount(this);

        binding.tvUserName.setText(name);
        binding.tvUserEmail.setText(email);
        binding.tvAvatarInitial.setText(emoji);
        binding.tvStatStreak.setText(streak + " ngày");
        binding.tvStatBills.setText(bills + " ảnh");
        binding.tvStatBudget.setText(UserManager.formatCurrency(budget));
    }

    private void applyAccentColor() {
        int accentColor = ThemeManager.getAccentColorInt(this);
        binding.ivProfileAvatar.setBackgroundTintList(ColorStateList.valueOf(accentColor));
        binding.btnQuickEditProfile.setTextColor(accentColor);
        binding.btnQuickEditProfile.setStrokeColor(ColorStateList.valueOf(accentColor));
        binding.btnQuickEditProfile.setIconTint(ColorStateList.valueOf(accentColor));
        binding.ivIconEditProfile.setImageTintList(ColorStateList.valueOf(accentColor));
    }

    private void setupListeners() {
        binding.btnBackProfile.setOnClickListener(v -> finish());

        // 1. Edit Profile (Triggered from Avatar, Quick Edit button, or Menu Row)
        Runnable openEditSheet = () -> {
            EditProfileBottomSheet bottomSheet = EditProfileBottomSheet.newInstance();
            bottomSheet.setOnProfileUpdatedListener(() -> {
                loadUserProfile();
                applyAccentColor();
            });
            bottomSheet.show(getSupportFragmentManager(), "EditProfileBottomSheet");
        };

        binding.layoutAvatarWrapper.setOnClickListener(v -> openEditSheet.run());
        binding.btnQuickEditProfile.setOnClickListener(v -> openEditSheet.run());
        binding.rowEditProfile.setOnClickListener(v -> openEditSheet.run());

        // 2. Friends & Shared Expenses
        binding.rowFriends.setOnClickListener(v -> showFriendsDialog());

        // 3. Privacy & Security
        binding.rowPrivacy.setOnClickListener(v -> showPrivacyDialog());

        // 4. About app
        binding.rowAbout.setOnClickListener(v -> showAboutDialog());

        // 5. Logout
        binding.rowLogout.setOnClickListener(v -> showLogoutDialog());
    }

    private void showFriendsDialog() {
        String friendCode = "SET-8899";
        new AlertDialog.Builder(this)
                .setTitle("Bạn bè & Chia sẻ chi tiêu")
                .setMessage("Mã kết nối cá nhân của bạn: " + friendCode + "\n\n" +
                        "Chia sẻ mã này với bạn bè để cùng theo dõi chi tiêu nhóm phong cách Locket!")
                .setPositiveButton("Sao chép mã", (dialog, which) -> {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("SET Friend Code", friendCode);
                    if (clipboard != null) {
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(this, "Đã sao chép mã kết nối: " + friendCode, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Đóng", null)
                .show();
    }

    private void showPrivacyDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Quyền riêng tư & Bảo mật")
                .setMessage("🔒 Dữ liệu chi tiêu & hình ảnh hóa đơn của bạn được lưu trữ an toàn và bảo mật trên thiết bị.\n\n" +
                        "• Mã hóa dữ liệu cục bộ: Đang bật\n" +
                        "• Sao lưu tự động: Hàng ngày\n" +
                        "• Bảo vệ sinh trắc học / PIN: Khả dụng")
                .setPositiveButton("Đã hiểu", null)
                .show();
    }

    private void showAboutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Về ứng dụng SET")
                .setMessage("Smart Expense Tracker v1.0\n\n" +
                        "Ứng dụng quản lý tài chính thông minh phong cách Locket.\n\n" +
                        "Tính năng nổi bật:\n" +
                        "• Chụp ảnh hóa đơn tỉ lệ 3:4\n" +
                        "• Timeline lịch sử & Lưới widget trực quan\n" +
                        "• Dashboard thống kê & Lọc theo dải ngày\n" +
                        "• Trợ lý AI tư vấn tài chính thông minh\n" +
                        "• Tùy chỉnh màu sắc cá nhân hóa\n\n" +
                        "Developed with ❤️ for Smart Spenders")
                .setPositiveButton("Đóng", null)
                .show();
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất khỏi tài khoản này không?")
                .setPositiveButton("Đăng xuất", (dialog, which) -> {
                    Toast.makeText(this, "Đã đăng xuất thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
