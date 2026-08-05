package com.smartexpense.android.presentation.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.smartexpense.android.databinding.ItemHistoryGridBinding;
import com.smartexpense.android.presentation.util.ThemeManager;
import java.util.List;

public class HistoryGridAdapter extends RecyclerView.Adapter<HistoryGridAdapter.ViewHolder> {

    private final List<ExpenseHistoryItem> items;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener clickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    public HistoryGridAdapter(List<ExpenseHistoryItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHistoryGridBinding binding = ItemHistoryGridBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(items.get(position), position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemHistoryGridBinding binding;

        public ViewHolder(ItemHistoryGridBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ExpenseHistoryItem item, int position) {
            Context context = binding.getRoot().getContext();
            int accentColor = ThemeManager.getAccentColorInt(context);

            int[] darkBgColors = {0xFF1A1A2E, 0xFF16213E, 0xFF0F3460, 0xFF1B1B2F, 0xFF2C2C54, 0xFF1E1E3F};
            binding.ivGridPhoto.setBackgroundColor(darkBgColors[position % darkBgColors.length]);
            binding.ivGridPhoto.setImageDrawable(null);

            binding.tvGridCaption.setText(item.getCaption());
            binding.tvGridAmount.setText(item.getAmount());
            binding.tvGridAmount.setTextColor(accentColor);

            binding.getRoot().setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onItemClick(position);
                }
            });
        }
    }
}
