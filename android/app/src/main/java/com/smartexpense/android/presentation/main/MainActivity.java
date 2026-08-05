package com.smartexpense.android.presentation.main;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;
import com.smartexpense.android.R;
import com.smartexpense.android.databinding.ActivityMainBinding;
import com.smartexpense.android.databinding.ViewLocketBottomBarBinding;
import com.smartexpense.android.presentation.camera.CameraFragment;
import com.smartexpense.android.presentation.history.WidgetGridFragment;
import com.smartexpense.android.presentation.profile.ProfileActivity;
import com.smartexpense.android.presentation.util.ThemeColorBottomSheet;
import com.smartexpense.android.presentation.util.ThemeManager;

/**
 * MainActivity — Pure Locket Architecture:
 * - Persistent Top Bar & Bottom Tab across all 4 screens
 * - ViewPager2 horizontal navigation matching Bottom Bar:
 *     [0: Lưới Widget <-> 1: Thống kê <-> 2: Camera & Timeline Feed <-> 3: Trợ lý AI]
 */
public class MainActivity extends AppCompatActivity implements CameraFragment.OnFeedPageChangeListener {

    private ActivityMainBinding binding;
    private ViewLocketBottomBarBinding bottomBar;
    private MainPagerAdapter pagerAdapter;
    private int currentPage = 2; // Default: Camera & Feed (Index 2)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bottomBar = ViewLocketBottomBarBinding.bind(binding.bottomBarInclude.getRoot());

        setupTopBar();
        setupViewPager();
        setupBottomBar();
        setupBackHandler();
        setupKeyboardInsets();
        applyAccentColor();
    }

    // ─────────────────────────────────────────────────────
    // Persistent Top Bar Setup
    // ─────────────────────────────────────────────────────

    private void setupTopBar() {
        binding.ivTopAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        });

        updateNotificationBadge();

        binding.btnTopNotifications.setOnClickListener(v -> {
            com.smartexpense.android.presentation.notification.NotificationBottomSheet bottomSheet =
                    com.smartexpense.android.presentation.notification.NotificationBottomSheet.newInstance();
            bottomSheet.setOnNotificationActionListener(tabIndex -> {
                binding.viewPager.setCurrentItem(tabIndex, true);
            });
            bottomSheet.setOnDismissListener(this::updateNotificationBadge);
            bottomSheet.show(getSupportFragmentManager(), "NotificationBottomSheet");
        });

        binding.btnFilterDropdown.setOnClickListener(v -> {
            if (currentPage == 0) {
                WidgetGridFragment gridFragment = pagerAdapter != null ? pagerAdapter.getWidgetGridFragment() : null;
                if (gridFragment != null) {
                    gridFragment.showCategoryFilterDialog();
                }
            } else if (currentPage == 2) {
                CameraFragment cameraFragment = pagerAdapter != null ? pagerAdapter.getCameraFragment() : null;
                if (cameraFragment != null) {
                    cameraFragment.showCategoryFilterDialog();
                }
            }
        });
    }

    // ─────────────────────────────────────────────────────
    // ViewPager2: [0: Widget <-> 1: Dashboard <-> 2: Camera <-> 3: Chat]
    // ─────────────────────────────────────────────────────

    private void setupViewPager() {
        pagerAdapter = new MainPagerAdapter(this);
        binding.viewPager.setAdapter(pagerAdapter);
        binding.viewPager.setOffscreenPageLimit(3);

        // Default: Camera tab (Index 2)
        binding.viewPager.setCurrentItem(2, false);
        currentPage = 2;

        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                syncUIForCurrentPage();
            }
        });

        binding.viewPager.post(() -> {
            // Setup fragment listeners once fragments are attached
            WidgetGridFragment gridFragment = pagerAdapter.getWidgetGridFragment();
            if (gridFragment != null) {
                gridFragment.setOnWidgetGridItemClickListener(position -> {
                    // Tap grid item -> jump directly to Camera feed at that item
                    binding.viewPager.setCurrentItem(2, false);
                    CameraFragment cameraFragment = pagerAdapter.getCameraFragment();
                    if (cameraFragment != null) {
                        cameraFragment.scrollToFeedPosition(position + 1);
                    }
                });
                gridFragment.setOnCategoryChangeListener(category -> {
                    if (currentPage == 0) {
                        binding.tvFilterTitle.setText(category + " ▼");
                    }
                });
            }

            CameraFragment cameraFragment = pagerAdapter.getCameraFragment();
            if (cameraFragment != null) {
                cameraFragment.setOnFeedPageChangeListener(this);
                cameraFragment.setOnCategoryChangeListener(category -> {
                    if (currentPage == 2) {
                        binding.tvFilterTitle.setText(category + " ▼");
                    }
                });
            }

            syncUIForCurrentPage();
        });
    }

    private void syncUIForCurrentPage() {
        WidgetGridFragment gridFragment = pagerAdapter != null ? pagerAdapter.getWidgetGridFragment() : null;
        CameraFragment cameraFragment = pagerAdapter != null ? pagerAdapter.getCameraFragment() : null;

        if (currentPage == 0) {
            // Tab 0: Lưới Widget (Widget Grid)
            binding.tvTopTitle.setVisibility(View.GONE);
            binding.btnFilterDropdown.setVisibility(View.VISIBLE);
            String cat = gridFragment != null ? gridFragment.getSelectedCategory() : "Tất cả";
            binding.tvFilterTitle.setText(cat + " ▼");
            updateBottomBarSelection(0);
            showCenterButton(true);
        } else if (currentPage == 1) {
            // Tab 1: Thống kê (Dashboard)
            binding.tvTopTitle.setText(getString(R.string.dashboard_title));
            binding.tvTopTitle.setVisibility(View.VISIBLE);
            binding.btnFilterDropdown.setVisibility(View.GONE);
            updateBottomBarSelection(1);
            showCenterButton(true);
        } else if (currentPage == 2) {
            // Tab 2: Camera & Timeline Feed
            int feedPos = cameraFragment != null ? cameraFragment.getCurrentFeedPosition() : 0;
            String cat = cameraFragment != null ? cameraFragment.getSelectedCategory() : "Tất cả";

            if (feedPos > 0) {
                binding.tvTopTitle.setVisibility(View.GONE);
                binding.btnFilterDropdown.setVisibility(View.VISIBLE);
                binding.tvFilterTitle.setText(cat + " ▼");
                updateBottomBarSelection(2);
                showCenterButton(true);
            } else {
                binding.tvTopTitle.setText(getString(R.string.app_name));
                binding.tvTopTitle.setVisibility(View.VISIBLE);
                binding.btnFilterDropdown.setVisibility(View.GONE);
                updateBottomBarSelection(2);
                showCenterButton(false);
            }
        } else if (currentPage == 3) {
            // Tab 3: Trợ lý AI (Chatbot)
            binding.tvTopTitle.setText(getString(R.string.chat_title));
            binding.tvTopTitle.setVisibility(View.VISIBLE);
            binding.btnFilterDropdown.setVisibility(View.GONE);
            updateBottomBarSelection(3);
            showCenterButton(true);
        }
    }

    // ─────────────────────────────────────────────────────
    // CameraFragment.OnFeedPageChangeListener
    // ─────────────────────────────────────────────────────

    @Override
    public void onFeedPageSelected(int position, String category) {
        if (currentPage == 2) {
            if (position > 0) {
                binding.tvTopTitle.setVisibility(View.GONE);
                binding.btnFilterDropdown.setVisibility(View.VISIBLE);
                binding.tvFilterTitle.setText(category + " ▼");
                updateBottomBarSelection(2);
                showCenterButton(true);
            } else {
                binding.tvTopTitle.setText(getString(R.string.app_name));
                binding.tvTopTitle.setVisibility(View.VISIBLE);
                binding.btnFilterDropdown.setVisibility(View.GONE);
                updateBottomBarSelection(2);
                showCenterButton(false);
            }
        }
    }

    private void showCenterButton(boolean show) {
        if (bottomBar == null) return;

        if (show) {
            if (bottomBar.btnNavShutter.getVisibility() != View.VISIBLE) {
                bottomBar.btnNavShutter.setVisibility(View.VISIBLE);
                bottomBar.btnNavShutter.setAlpha(0f);
                bottomBar.btnNavShutter.setScaleX(0.7f);
                bottomBar.btnNavShutter.setScaleY(0.7f);
                bottomBar.btnNavShutter.animate()
                        .alpha(1f)
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(180)
                        .start();
            }
        } else {
            if (bottomBar.btnNavShutter.getVisibility() == View.VISIBLE) {
                bottomBar.btnNavShutter.animate()
                        .alpha(0f)
                        .scaleX(0.7f)
                        .scaleY(0.7f)
                        .setDuration(150)
                        .withEndAction(() -> bottomBar.btnNavShutter.setVisibility(View.INVISIBLE))
                        .start();
            } else {
                bottomBar.btnNavShutter.setVisibility(View.INVISIBLE);
            }
        }
    }

    // ─────────────────────────────────────────────────────
    // Locket-style Bottom Navigation Bar
    // ─────────────────────────────────────────────────────

    private void setupBottomBar() {
        // Slot 1: Widget Grid (Tab 0)
        bottomBar.btnNavGrid.setOnClickListener(v -> binding.viewPager.setCurrentItem(0, true));

        // Slot 2: Dashboard (Tab 1)
        bottomBar.btnNavDashboard.setOnClickListener(v -> binding.viewPager.setCurrentItem(1, true));

        // Slot 3: Center Shutter (Camera Tab 2, live preview at pos 0)
        bottomBar.btnNavShutter.setOnClickListener(v -> {
            if (currentPage != 2) {
                binding.viewPager.setCurrentItem(2, true);
            }
            CameraFragment cameraFragment = pagerAdapter != null ? pagerAdapter.getCameraFragment() : null;
            if (cameraFragment != null) {
                cameraFragment.scrollToCamera();
            }
        });

        // Slot 4: Chatbot AI (Tab 3)
        bottomBar.btnNavChat.setOnClickListener(v -> binding.viewPager.setCurrentItem(3, true));

        // Slot 5: Palette / Theme Color Picker
        bottomBar.btnNavPalette.setOnClickListener(v -> showColorPaletteBottomSheet());

        // Default: Camera active
        updateBottomBarSelection(2);
        showCenterButton(false);
    }

    private void updateBottomBarSelection(int page) {
        int accentColor = ThemeManager.getAccentColorInt(this);
        float activeAlpha = 1.0f;
        float inactiveAlpha = 0.55f;

        // Slot 1: Grid icon (Page 0)
        boolean isGridActive = (page == 0);
        bottomBar.ivNavGrid.setAlpha(isGridActive ? activeAlpha : inactiveAlpha);
        bottomBar.ivNavGrid.setImageTintList(isGridActive ? ColorStateList.valueOf(accentColor) : null);

        // Slot 2: Dashboard icon (Page 1)
        boolean isDashboardActive = (page == 1);
        bottomBar.ivNavDashboard.setAlpha(isDashboardActive ? activeAlpha : inactiveAlpha);
        bottomBar.ivNavDashboard.setImageTintList(isDashboardActive ? ColorStateList.valueOf(accentColor) : null);

        // Slot 4: Chat icon (Page 3)
        boolean isChatActive = (page == 3);
        bottomBar.ivNavChat.setAlpha(isChatActive ? activeAlpha : inactiveAlpha);
        bottomBar.ivNavChat.setImageTintList(isChatActive ? ColorStateList.valueOf(accentColor) : null);

        // Slot 5: Palette icon
        bottomBar.ivNavPalette.setAlpha(inactiveAlpha);
    }

    // ─────────────────────────────────────────────────────
    // Keyboard Insets Handling (for Chat tab)
    // ─────────────────────────────────────────────────────

    private void setupKeyboardInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            boolean isKeyboardVisible = insets.isVisible(WindowInsetsCompat.Type.ime());
            if (currentPage == 3) {
                // In Chat tab: hide bottom navigation bar when soft keyboard is open
                binding.bottomBarInclude.getRoot().setVisibility(isKeyboardVisible ? View.GONE : View.VISIBLE);
            } else {
                binding.bottomBarInclude.getRoot().setVisibility(View.VISIBLE);
            }
            return insets;
        });
    }

    // ─────────────────────────────────────────────────────
    // Back Press Dispatcher
    // ─────────────────────────────────────────────────────

    private void setupBackHandler() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                CameraFragment cameraFragment = pagerAdapter != null ? pagerAdapter.getCameraFragment() : null;
                if (currentPage == 2) {
                    if (cameraFragment != null && cameraFragment.getCurrentFeedPosition() > 0) {
                        cameraFragment.scrollToCamera();
                    } else {
                        setEnabled(false);
                        getOnBackPressedDispatcher().onBackPressed();
                    }
                } else {
                    binding.viewPager.setCurrentItem(2, true);
                }
            }
        });
    }

    // ─────────────────────────────────────────────────────
    // Theme Accent Tint
    // ─────────────────────────────────────────────────────

    private void applyAccentColor() {
        int accentColor = ThemeManager.getAccentColorInt(this);
        binding.ivTopAvatar.setBackgroundTintList(ColorStateList.valueOf(accentColor));
        binding.tvTopTitle.setTextColor(accentColor);
        bottomBar.viewShutterRing.setBackgroundTintList(ColorStateList.valueOf(accentColor));
        bottomBar.badgeChat.setBackgroundTintList(ColorStateList.valueOf(accentColor));

        updateBottomBarSelection(currentPage);

        if (pagerAdapter != null) {
            if (pagerAdapter.getWidgetGridFragment() != null) pagerAdapter.getWidgetGridFragment().applyAccentColor();
            if (pagerAdapter.getDashboardFragment() != null) pagerAdapter.getDashboardFragment().applyAccentColor();
            if (pagerAdapter.getCameraFragment() != null) pagerAdapter.getCameraFragment().applyAccentColor();
            if (pagerAdapter.getChatFragment() != null) pagerAdapter.getChatFragment().applyAccentColor();
        }
    }

    private void showColorPaletteBottomSheet() {
        ThemeColorBottomSheet bottomSheet = ThemeColorBottomSheet.newInstance();
        bottomSheet.setOnColorSelectedListener(color -> applyAccentColor());
        bottomSheet.show(getSupportFragmentManager(), "ThemeColorBottomSheet");
    }

    public void updateNotificationBadge() {
        int unread = com.smartexpense.android.presentation.notification.NotificationManager.getInstance().getUnreadCount();
        binding.viewNotificationBadge.setVisibility(unread > 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        applyAccentColor();
        updateNotificationBadge();
    }
}
