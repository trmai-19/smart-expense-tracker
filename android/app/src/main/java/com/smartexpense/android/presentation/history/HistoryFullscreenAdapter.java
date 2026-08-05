package com.smartexpense.android.presentation.history;

import android.content.Context;
import android.content.res.ColorStateList;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.smartexpense.android.databinding.ItemHistoryFullscreenBinding;
import com.smartexpense.android.presentation.util.ThemeManager;
import java.util.List;

public class HistoryFullscreenAdapter extends RecyclerView.Adapter<HistoryFullscreenAdapter.ViewHolder> {

    private final List<ExpenseHistoryItem> items;
    private final Context context;

    public HistoryFullscreenAdapter(Context context, List<ExpenseHistoryItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHistoryFullscreenBinding binding = ItemHistoryFullscreenBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemHistoryFullscreenBinding binding;

        public ViewHolder(ItemHistoryFullscreenBinding binding) {
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
