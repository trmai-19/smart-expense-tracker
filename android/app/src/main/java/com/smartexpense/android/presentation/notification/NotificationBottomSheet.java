package com.smartexpense.android.presentation.notification;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.smartexpense.android.R;
import com.smartexpense.android.data.model.NotificationItem;
import com.smartexpense.android.databinding.BottomSheetNotificationsBinding;
import com.smartexpense.android.presentation.util.ThemeManager;
import java.util.List;

public class NotificationBottomSheet extends BottomSheetDialogFragment {

    public interface OnNotificationActionListener {
        void onNavigateToTab(int tabIndex);
    }

    public interface OnDismissListener {
        void onDismiss();
    }

    private BottomSheetNotificationsBinding binding;
    private NotificationAdapter adapter;
    private List<NotificationItem> notificationList;
    private OnNotificationActionListener actionListener;
    private OnDismissListener dismissListener;

    public static NotificationBottomSheet newInstance() {
        return new NotificationBottomSheet();
    }

    public void setOnNotificationActionListener(OnNotificationActionListener listener) {
        this.actionListener = listener;
    }

    public void setOnDismissListener(OnDismissListener listener) {
        this.dismissListener = listener;
    }

    @Override
    public int getTheme() {
        return R.style.ThemeOverlay_SET_BottomSheetDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetNotificationsBinding.inflate(inflater, container, false);
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

        int accentColor = ThemeManager.getAccentColorInt(requireContext());
        binding.ivHeaderBell.setImageTintList(ColorStateList.valueOf(accentColor));
        binding.btnMarkAllRead.setTextColor(accentColor);

        setupRecyclerView();
        setupListeners();
        updateHeaderCount();
    }

    private void setupRecyclerView() {
        notificationList = NotificationManager.getInstance().getNotifications();

        if (notificationList.isEmpty()) {
            binding.rvNotifications.setVisibility(View.GONE);
            binding.layoutEmptyState.setVisibility(View.VISIBLE);
            return;
        }

        binding.rvNotifications.setVisibility(View.VISIBLE);
        binding.layoutEmptyState.setVisibility(View.GONE);

        adapter = new NotificationAdapter(requireContext(), notificationList, item -> {
            NotificationManager.getInstance().markAsRead(item.getId());
            updateHeaderCount();

            if (item.getTargetTab() != null) {
                dismiss();
                if (actionListener != null) {
                    actionListener.onNavigateToTab(item.getTargetTab());
                }
            } else {
                Toast.makeText(requireContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        binding.rvNotifications.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvNotifications.setAdapter(adapter);
    }

    private void setupListeners() {
        binding.btnMarkAllRead.setOnClickListener(v -> {
            NotificationManager.getInstance().markAllAsRead();
            for (NotificationItem item : notificationList) {
                item.setRead(true);
            }
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
            updateHeaderCount();
            Toast.makeText(requireContext(), "Đã đánh dấu tất cả là đã đọc", Toast.LENGTH_SHORT).show();
        });
    }

    private void updateHeaderCount() {
        int unread = NotificationManager.getInstance().getUnreadCount();
        if (unread > 0) {
            binding.tvSheetSubtitle.setText(unread + " thông báo chưa đọc · Cập nhật tài chính & AI");
            binding.btnMarkAllRead.setVisibility(View.VISIBLE);
        } else {
            binding.tvSheetSubtitle.setText("Tất cả thông báo đã được đọc");
            binding.btnMarkAllRead.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (dismissListener != null) {
            dismissListener.onDismiss();
        }
    }
}
