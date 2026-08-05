package com.smartexpense.android.presentation.chat;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.smartexpense.android.R;
import com.smartexpense.android.databinding.FragmentChatBinding;
import com.smartexpense.android.presentation.util.ThemeManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatFragment extends Fragment {

    private FragmentChatBinding binding;
    private ChatAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
        setupListeners();
        applyAccentColor();
    }

    public void applyAccentColor() {
        if (getContext() == null || binding == null) return;
        int accentColor = ThemeManager.getAccentColorInt(requireContext());
        binding.btnSendMessage.setBackgroundTintList(ColorStateList.valueOf(accentColor));
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void setupRecyclerView() {
        if (getContext() == null) return;
        binding.rvChatMessages.setLayoutManager(new LinearLayoutManager(requireContext()));
        List<ChatMessage> initial = new ArrayList<>();
        initial.add(new ChatMessage(getString(R.string.bot_welcome_message), false, getCurrentTime()));
        adapter = new ChatAdapter(requireContext(), initial);
        binding.rvChatMessages.setAdapter(adapter);
    }

    private void setupListeners() {
        binding.btnSendMessage.setOnClickListener(v -> sendMessage(null));

        binding.etMessageInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && adapter != null && adapter.getItemCount() > 0) {
                binding.rvChatMessages.postDelayed(() -> {
                    if (binding != null && adapter != null) {
                        binding.rvChatMessages.smoothScrollToPosition(adapter.getItemCount() - 1);
                    }
                }, 200);
            }
        });

        binding.chipPromptWeek.setOnClickListener(v -> sendMessage("Chi tiêu tuần này của tôi như thế nào?"));
        binding.chipPromptTips.setOnClickListener(v -> sendMessage("Gợi ý cách tiết kiệm tiền hiệu quả cho tôi"));
        binding.chipPromptFood.setOnClickListener(v -> sendMessage("Tháng này tôi đã chi bao nhiêu cho ăn uống?"));
    }

    private void sendMessage(@Nullable String presetMessage) {
        String input;
        if (presetMessage != null) {
            input = presetMessage;
        } else {
            input = binding.etMessageInput.getText() != null
                    ? binding.etMessageInput.getText().toString().trim()
                    : "";
        }

        if (input.isEmpty()) return;

        // Add user message
        adapter.addMessage(new ChatMessage(input, true, getCurrentTime()));
        if (presetMessage == null) {
            binding.etMessageInput.setText("");
        }
        binding.rvChatMessages.smoothScrollToPosition(adapter.getItemCount() - 1);

        // Simulated Gemini AI response after short delay
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (getContext() == null || binding == null) return;
            String botReply = generateBotReply(input);
            adapter.addMessage(new ChatMessage(botReply, false, getCurrentTime()));
            binding.rvChatMessages.smoothScrollToPosition(adapter.getItemCount() - 1);
        }, 800);
    }

    private String generateBotReply(String userInput) {
        String lower = userInput.toLowerCase(Locale.ROOT);
        if (lower.contains("ăn") || lower.contains("uống") || lower.contains("food")) {
            return "Tháng này bạn đã chi 450.000 ₫ cho Ăn uống (chiếm 45% tổng chi tiêu). Bạn đang giữ mức ăn uống khá hợp lý!";
        } else if (lower.contains("tuần") || lower.contains("week")) {
            return "Tổng chi tiêu tuần này của bạn là 320.000 ₫. Bạn đã tiết kiệm hơn tuần trước 15%!";
        } else if (lower.contains("tiết kiệm") || lower.contains("mẹo")) {
            return "Mẹo tiết kiệm tuần này: Hạn chế gọi đồ uống ngoài và tự chuẩn bị bữa trưa có thể giúp bạn tiết kiệm khoảng 200.000 ₫/tuần đấy!";
        } else if (lower.contains("tổng") || lower.contains("tháng")) {
            return "Tổng chi tiêu tháng này của bạn là 1.250.000 ₫, nằm trong hạn mức ngân sách an toàn!";
        } else {
            return "Tôi đã ghi nhận chi tiêu của bạn. Trợ lý Gemini AI sẽ tiếp tục học hỏi và phân tích thói quen tài chính để đưa ra đề xuất tối ưu nhất!";
        }
    }

    private String getCurrentTime() {
        return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
    }

    @Override
    public void onResume() {
        super.onResume();
        applyAccentColor();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
