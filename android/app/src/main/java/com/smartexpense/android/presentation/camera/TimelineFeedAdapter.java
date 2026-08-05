package com.smartexpense.android.presentation.camera;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.camera.view.PreviewView;
import androidx.recyclerview.widget.RecyclerView;
import com.smartexpense.android.databinding.ItemFeedCameraBinding;
import com.smartexpense.android.databinding.ItemHistoryFullscreenBinding;
import com.smartexpense.android.presentation.history.ExpenseHistoryItem;
import com.smartexpense.android.presentation.util.ThemeManager;
import java.util.List;

/**
 * TimelineFeedAdapter — Pure Locket Vertical Feed:
 * Position 0: Live Camera Viewfinder & Controls
 * Position 1..N: Expense Timeline Photo Cards
 */
public class TimelineFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int VIEW_TYPE_CAMERA = 0;
    public static final int VIEW_TYPE_EXPENSE = 1;

    public interface FeedCameraListener {
        void onShutterClicked();
        void onGalleryClicked();
        void onFlipCameraClicked();
        void onScrollToHistoryClicked();
        void onPreviewBound(PreviewView previewView);
    }

    private final Context context;
    private final List<ExpenseHistoryItem> expenseItems;
    private FeedCameraListener cameraListener;

    public TimelineFeedAdapter(Context context, List<ExpenseHistoryItem> expenseItems) {
        this.context = context;
        this.expenseItems = expenseItems;
    }

    public void setCameraListener(FeedCameraListener listener) {
        this.cameraListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_CAMERA : VIEW_TYPE_EXPENSE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_CAMERA) {
            ItemFeedCameraBinding binding = ItemFeedCameraBinding.inflate(inflater, parent, false);
            return new CameraViewHolder(binding);
        } else {
            ItemHistoryFullscreenBinding binding = ItemHistoryFullscreenBinding.inflate(inflater, parent, false);
            return new ExpenseViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CameraViewHolder) {
            ((CameraViewHolder) holder).bind();
        } else if (holder instanceof ExpenseViewHolder) {
            // Expense index is position - 1
            ExpenseHistoryItem item = expenseItems.get(position - 1);
            ((ExpenseViewHolder) holder).bind(item);
        }
    }

    @Override
    public int getItemCount() {
        return 1 + expenseItems.size();
    }

    // ─────────────────────────────────────────────
    // ViewHolders
    // ─────────────────────────────────────────────

    public class CameraViewHolder extends RecyclerView.ViewHolder {
        final ItemFeedCameraBinding binding;

        public CameraViewHolder(ItemFeedCameraBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.layoutShutter.setOnClickListener(v -> {
                if (cameraListener != null) cameraListener.onShutterClicked();
            });

            binding.btnGallery.setOnClickListener(v -> {
                if (cameraListener != null) cameraListener.onGalleryClicked();
            });

            binding.btnFlipCamera.setOnClickListener(v -> {
                if (cameraListener != null) cameraListener.onFlipCameraClicked();
            });

            binding.tvHistoryHint.setOnClickListener(v -> {
                if (cameraListener != null) cameraListener.onScrollToHistoryClicked();
            });
        }

        public void bind() {
            int accentColor = ThemeManager.getAccentColorInt(context);
            binding.viewShutterRing.setBackgroundTintList(ColorStateList.valueOf(accentColor));
            binding.tvHistoryHint.setTextColor(accentColor);

            if (cameraListener != null) {
                cameraListener.onPreviewBound(binding.previewView);
            }
        }
    }

    public class ExpenseViewHolder extends RecyclerView.ViewHolder {
        final ItemHistoryFullscreenBinding binding;

        public ExpenseViewHolder(ItemHistoryFullscreenBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ExpenseHistoryItem item) {
            int accentColor = ThemeManager.getAccentColorInt(context);

            // Distinct background placeholder color
            int[] darkBgColors = {0xFF1A1A2E, 0xFF16213E, 0xFF0F3460, 0xFF1B1B2F, 0xFF2C2C54, 0xFF1E1E3F};
            int colorIndex = Math.abs(item.getId().hashCode()) % darkBgColors.length;
            binding.ivFullscreenPhoto.setBackgroundColor(darkBgColors[colorIndex]);
            binding.ivFullscreenPhoto.setImageDrawable(null);

            // Caption pill on photo
            binding.tvFullscreenCaption.setText(item.getCaption());

            // Centered User Name & Avatar: Sync avatar ring with system accent color & show user name
            String userName = com.smartexpense.android.presentation.util.UserManager.getUserName(context);
            binding.tvSenderName.setText(userName);
            binding.ivSenderAvatar.setBackgroundTintList(ColorStateList.valueOf(accentColor));
            binding.ivSenderAvatar.setImageTintList(ColorStateList.valueOf(0xFF0D0D12));

            // Single Combined Info Line: "Ăn uống · Hôm nay lúc 12:30 · 50.000 ₫"
            String prefix = item.getCategory() + "  ·  " + item.getTimeAgo() + "  ·  ";
            String amount = item.getAmount();
            String fullText = prefix + amount;

            SpannableString spannable = new SpannableString(fullText);
            int startAmount = prefix.length();
            int endAmount = fullText.length();

            // Highlight amount in Accent color and Bold
            spannable.setSpan(new ForegroundColorSpan(accentColor), startAmount, endAmount, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new StyleSpan(Typeface.BOLD), startAmount, endAmount, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            binding.tvCombinedExpenseInfo.setText(spannable);
        }
    }
}
