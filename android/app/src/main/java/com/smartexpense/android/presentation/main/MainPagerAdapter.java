package com.smartexpense.android.presentation.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.smartexpense.android.presentation.camera.CameraFragment;
import com.smartexpense.android.presentation.chat.ChatFragment;
import com.smartexpense.android.presentation.dashboard.DashboardFragment;
import com.smartexpense.android.presentation.history.WidgetGridFragment;

/**
 * MainPagerAdapter — Pure Locket Navigation matching Bottom Bar order:
 * - Tab 0: Widget Grid (Lưới ảnh chi tiêu / Widget History)
 * - Tab 1: Dashboard (Thống kê chi tiêu)
 * - Tab 2: Camera & Locket Timeline Feed (Camera chụp ảnh + Lướt dọc xem chi tiết)
 * - Tab 3: Chatbot AI (Trợ lý tài chính thông minh)
 */
public class MainPagerAdapter extends FragmentStateAdapter {

    private WidgetGridFragment widgetGridFragment;
    private DashboardFragment dashboardFragment;
    private CameraFragment cameraFragment;
    private ChatFragment chatFragment;

    public MainPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                widgetGridFragment = new WidgetGridFragment();
                return widgetGridFragment;
            case 1:
                dashboardFragment = new DashboardFragment();
                return dashboardFragment;
            case 2:
                cameraFragment = new CameraFragment();
                return cameraFragment;
            case 3:
                chatFragment = new ChatFragment();
                return chatFragment;
            default:
                cameraFragment = new CameraFragment();
                return cameraFragment;
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public WidgetGridFragment getWidgetGridFragment() {
        return widgetGridFragment;
    }

    public DashboardFragment getDashboardFragment() {
        return dashboardFragment;
    }

    public CameraFragment getCameraFragment() {
        return cameraFragment;
    }

    public ChatFragment getChatFragment() {
        return chatFragment;
    }
}
