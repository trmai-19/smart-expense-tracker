package com.smartexpense.android.presentation.notification;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.smartexpense.android.data.model.NotificationItem;
import com.smartexpense.android.databinding.ItemNotificationBinding;
import com.smartexpense.android.presentation.util.ThemeManager;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    public interface OnNotificationClickListener {
        void onNotificationClick(NotificationItem item);
    }

    private final Context context;
    private final List<NotificationItem> items;
    private final OnNotificationClickListener listener;

    public NotificationAdapter(Context context, List<NotificationItem> items, OnNotificationClickListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNotificationBinding binding = ItemNotificationBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new NotificationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {
        private final ItemNotificationBinding binding;

        NotificationViewHolder(ItemNotificationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(NotificationItem item) {
            int accentColor = ThemeManager.getAccentColorInt(context);

            binding.tvNotificationTitle.setText(item.getTitle());
            binding.tvNotificationMessage.setText(item.getMessage());
            binding.tvNotificationTime.setText(item.getTimeAgo());

            // Unread dot indicator
            binding.viewUnreadDot.setVisibility(item.isRead() ? View.GONE : View.VISIBLE);
            binding.viewUnreadDot.setBackgroundTintList(ColorStateList.valueOf(accentColor));

            // Card transparency / appearance based on read state
            binding.cardNotification.setAlpha(item.isRead() ? 0.75f : 1.0f);

            // Configure icon, tag, colors based on Type
            String emoji = "🔔";
            String tag = "THÔNG BÁO";
            int tagColor = accentColor;
            String actionHint = "👉 Nhấn để xem chi tiết";

            switch (item.getType()) {
                case BUDGET_ALERT:
                    emoji = "⚠️";
                    tag = "CẢNH BÁO";
                    tagColor = Color.parseColor("#FF9500");
                    actionHint = "👉 Nhấn để xem Thống kê chi tiết";
                    break;
                case AI_INSIGHT:
                    emoji = "💡";
                    tag = "TRỢ LÝ AI";
                    tagColor = accentColor;
                    actionHint = "👉 Trò chuyện cùng Trợ lý AI";
                    break;
                case REMINDER:
                    emoji = "📸";
                    tag = "NHẮC NHỞ";
                    tagColor = Color.parseColor("#34C759");
                    actionHint = "👉 Mở Camera chụp ảnh ngay";
                    break;
                case WEEKLY_REPORT:
                    emoji = "📊";
                    tag = "BÁO CÁO";
                    tagColor = Color.parseColor("#0A84FF");
                    actionHint = "👉 Xem báo cáo tuần qua";
                    break;
                case SYSTEM:
                    emoji = "✨";
                    tag = "HỆ THỐNG";
                    tagColor = Color.parseColor("#BF5AF2");
                    actionHint = "👉 Khám phá tính năng mới";
                    break;
            }

            binding.tvNotificationIconEmoji.setText(emoji);
            binding.tvTypeTag.setText(tag);
            binding.tvTypeTag.setTextColor(tagColor);

            binding.tvActionHint.setText(actionHint);
            binding.tvActionHint.setTextColor(tagColor);
            binding.tvActionHint.setVisibility(item.getTargetTab() != null ? View.VISIBLE : View.GONE);

            binding.cardNotification.setOnClickListener(v -> {
                if (!item.isRead()) {
                    item.setRead(true);
                    notifyItemChanged(getBindingAdapterPosition());
                }
                if (listener != null) {
                    listener.onNotificationClick(item);
                }
            });
        }
    }
}
