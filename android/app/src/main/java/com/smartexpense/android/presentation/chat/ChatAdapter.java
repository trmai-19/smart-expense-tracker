package com.smartexpense.android.presentation.chat;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.smartexpense.android.databinding.ItemChatMessageBinding;
import com.smartexpense.android.presentation.util.ThemeManager;
import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private final List<ChatMessage> messages = new ArrayList<>();
    private final Context context;

    public ChatAdapter(Context context, List<ChatMessage> initialMessages) {
        this.context = context;
        if (initialMessages != null) {
            this.messages.addAll(initialMessages);
        }
    }

    public void addMessage(ChatMessage message) {
        messages.add(message);
        notifyItemInserted(messages.size() - 1);
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemChatMessageBinding binding = ItemChatMessageBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ChatViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.bind(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {
        private final ItemChatMessageBinding binding;

        public ChatViewHolder(ItemChatMessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ChatMessage message) {
            int accentColor = ThemeManager.getAccentColorInt(context);

            if (message.isUser()) {
                binding.layoutUserMessage.setVisibility(View.VISIBLE);
                binding.layoutBotMessage.setVisibility(View.GONE);
                binding.tvUserText.setText(message.getText());
                binding.tvUserTime.setText(message.getTime());
                binding.layoutUserBubble.setBackgroundTintList(ColorStateList.valueOf(accentColor));
            } else {
                binding.layoutBotMessage.setVisibility(View.VISIBLE);
                binding.layoutUserMessage.setVisibility(View.GONE);
                binding.tvBotText.setText(message.getText());
                binding.tvBotTime.setText(message.getTime());
            }
        }
    }
}
