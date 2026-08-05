package com.smartexpense.android.presentation.chat;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.smartexpense.android.R;
import com.smartexpense.android.databinding.ActivityChatBinding;
import com.smartexpense.android.presentation.util.ThemeManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private ChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        applyAccentColor();
        setupRecyclerView();
        setupListeners();
    }

    private void applyAccentColor() {
        int accentColor = ThemeManager.getAccentColorInt(this);
        binding.btnSendMessage.setBackgroundTintList(ColorStateList.valueOf(accentColor));
    }

    private void setupRecyclerView() {
        binding.rvChatMessages.setLayoutManager(new LinearLayoutManager(this));
        List<ChatMessage> initial = new ArrayList<>();
        initial.add(new ChatMessage(getString(R.string.bot_welcome_message), false, getCurrentTime()));
        adapter = new ChatAdapter(this, initial);
        binding.rvChatMessages.setAdapter(adapter);
    }

    private void setupListeners() {
        binding.btnBackChat.setOnClickListener(v -> finish());

        binding.btnSendMessage.setOnClickListener(v -> sendMessage());
    }

    private void sendMessage() {
        String input = binding.etMessageInput.getText() != null
                ? binding.etMessageInput.getText().toString().trim()
                : "";

        if (input.isEmpty()) return;

        // Add user message
        adapter.addMessage(new ChatMessage(input, true, getCurrentTime()));
        binding.etMessageInput.setText("");
        binding.rvChatMessages.smoothScrollToPosition(adapter.getItemCount() - 1);

        // Simulated AI response after short delay
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            String botReply = generateBotReply(input);
            adapter.addMessage(new ChatMessage(botReply, false, getCurrentTime()));
            binding.rvChatMessages.smoothScrollToPosition(adapter.getItemCount() - 1);
        }, 800);
    }

    private String generateBotReply(String userInput) {
        String lower = userInput.toLowerCase(Locale.ROOT);
        if (lower.contains("ăn") || lower.contains("uống") || lower.contains("food")) {
            return getString(R.string.sample_bot_reply);
        } else if (lower.contains("tổng") || lower.contains("tháng")) {
            return "Tổng chi tiêu tháng này của bạn hiện tại là 1.250.000 ₫, giảm 12% so với cùng kỳ tháng trước!";
        } else {
            return "Tôi đã ghi nhận câu hỏi của bạn. Tính năng AI Gemini đầy đủ sẽ được tích hợp để tự động phân tích chi tiêu sâu hơn!";
        }
    }

    private String getCurrentTime() {
        return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
    }
}
