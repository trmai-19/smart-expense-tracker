package com.smartexpense.android.presentation.camera;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.smartexpense.android.databinding.ItemLocketHistoryCardBinding;
import com.smartexpense.android.databinding.PageCameraBinding;
import com.smartexpense.android.presentation.history.ExpenseHistoryItem;
import com.smartexpense.android.presentation.util.ThemeManager;
import java.util.List;

/**
 * Adapter for the Locket-style vertical ViewPager2 feed.
 *
 * Page 0   = Camera live preview (page_camera.xml)
 * Page 1+  = History cards (item_locket_history_card.xml)
 *
 * The camera page hosts the CameraX PreviewView; binding is exposed
 * so CameraFragment can attach the camera use cases.
 */
public class LocketFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_CAMERA  = 0;
    private static final int VIEW_TYPE_HISTORY = 1;

    private final Context context;
    private final List<ExpenseHistoryItem> historyItems;

    // Exposed so CameraFragment can attach PreviewView
    private PageCameraBinding cameraPageBinding;

    public interface OnCaptureClickListener {
        void onCaptureClick();
        void onGalleryClick();
        void onFlipClick();
    }

    private OnCaptureClickListener captureListener;

    public void setOnCaptureClickListener(OnCaptureClickListener l) {
        this.captureListener = l;
    }

    public LocketFeedAdapter(Context context, List<ExpenseHistoryItem> historyItems) {
        this.context = context;
        this.historyItems = historyItems;
    }

    public PageCameraBinding getCameraPageBinding() {
        return cameraPageBinding;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_CAMERA : VIEW_TYPE_HISTORY;
    }

    @Override
    public int getItemCount() {
        // 1 camera page + history pages
        return 1 + historyItems.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_CAMERA) {
            PageCameraBinding b = PageCameraBinding.inflate(inflater, parent, false);
            cameraPageBinding = b;
            return new CameraViewHolder(b);
        } else {
            ItemLocketHistoryCardBinding b = ItemLocketHistoryCardBinding.inflate(inflater, parent, false);
            return new HistoryViewHolder(b);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CameraViewHolder) {
            ((CameraViewHolder) holder).bind();
        } else if (holder instanceof HistoryViewHolder) {
            ((HistoryViewHolder) holder).bind(historyItems.get(position - 1));
        }
    }

    // ─── Camera ViewHolder ──────────────────────────────────────────────────

    class CameraViewHolder extends RecyclerView.ViewHolder {
        private final PageCameraBinding binding;

        CameraViewHolder(PageCameraBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind() {
            int accentColor = ThemeManager.getAccentColorInt(context);
            binding.ivTopAvatar.setBackgroundTintList(ColorStateList.valueOf(accentColor));

            binding.layoutShutter.setOnClickListener(v -> {
                if (captureListener != null) captureListener.onCaptureClick();
            });
            binding.btnGallery.setOnClickListener(v -> {
                if (captureListener != null) captureListener.onGalleryClick();
            });
            binding.btnFlipCamera.setOnClickListener(v -> {
                if (captureListener != null) captureListener.onFlipClick();
            });
        }
    }

    // ─── History ViewHolder ─────────────────────────────────────────────────

    class HistoryViewHolder extends RecyclerView.ViewHolder {
        private final ItemLocketHistoryCardBinding binding;

        HistoryViewHolder(ItemLocketHistoryCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ExpenseHistoryItem item) {
            int accentColor = ThemeManager.getAccentColorInt(context);
            int pos = getBindingAdapterPosition() - 1; // 0-indexed history

            // Unique dark background per card (placeholder until real photos)
            int[] bgColors = {0xFF1A1A2E, 0xFF16213E, 0xFF0F3460, 0xFF1B1B2F, 0xFF2C2C54, 0xFF1E1E3F};
            binding.ivHistoryPhoto.setBackgroundColor(bgColors[pos % bgColors.length]);
            binding.ivHistoryPhoto.setImageDrawable(null);

            binding.tvHistoryCaption.setText(item.getCaption());
            binding.tvSenderName.setText(com.smartexpense.android.presentation.util.UserManager.getUserName(context));
            binding.tvSenderTime.setText(item.getTimeAgo());

            binding.tvHistoryAmount.setText(item.getAmount());
            binding.tvHistoryAmount.setTextColor(accentColor);

            // Tint my avatar to accent
            binding.ivMyAvatar.setBackgroundTintList(ColorStateList.valueOf(accentColor));
        }
    }
}
