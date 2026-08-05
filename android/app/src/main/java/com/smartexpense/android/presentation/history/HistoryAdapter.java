package com.smartexpense.android.presentation.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.smartexpense.android.databinding.ItemHistoryWidgetBinding;
import com.smartexpense.android.presentation.util.ThemeManager;
import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private final List<ExpenseHistoryItem> items = new ArrayList<>();
    private final Context context;

    public HistoryAdapter(Context context, List<ExpenseHistoryItem> initialItems) {
        this.context = context;
        if (initialItems != null) {
            this.items.addAll(initialItems);
        }
    }

    public void addItem(ExpenseHistoryItem item) {
        this.items.add(0, item);
        notifyItemInserted(0);
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHistoryWidgetBinding binding = ItemHistoryWidgetBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new HistoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {
        private final ItemHistoryWidgetBinding binding;

        public HistoryViewHolder(ItemHistoryWidgetBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ExpenseHistoryItem item) {
            binding.tvHistoryCaption.setText(item.getCaption());
            binding.tvHistoryAmount.setText(item.getAmount());
            binding.tvHistoryMeta.setText(item.getCategory() + " · " + item.getTimeAgo());

            int accentColor = ThemeManager.getAccentColorInt(context);
            binding.tvHistoryAmount.setTextColor(accentColor);

            binding.btnActionDownload.setOnClickListener(v ->
                    Toast.makeText(context, "Đã lưu ảnh vào thư viện thiết bị", Toast.LENGTH_SHORT).show()
            );

            binding.btnActionShare.setOnClickListener(v ->
                    Toast.makeText(context, "Chia sẻ chi tiêu: " + item.getCaption(), Toast.LENGTH_SHORT).show()
            );

            binding.btnActionDelete.setOnClickListener(v -> {
                int pos = getBindingAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && pos < items.size()) {
                    items.remove(pos);
                    notifyItemRemoved(pos);
                    Toast.makeText(context, "Đã xóa khoản chi", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
