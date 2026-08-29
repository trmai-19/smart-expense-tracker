package com.smartexpense.android.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartexpense.android.domain.usecase.chat.GetChatHistoryUseCase
import com.smartexpense.android.domain.usecase.chat.SendChatMessageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class ChatViewModel(
    private val sendChatMessageUseCase: SendChatMessageUseCase,
    private val getChatHistoryUseCase: GetChatHistoryUseCase
) : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        // Lấy lịch sử khi mở màn hình
        loadHistory()
    }

    private fun loadHistory() {
        _isLoading.value = true
        viewModelScope.launch {
            val result = getChatHistoryUseCase.execute()
            _isLoading.value = false
            result.onSuccess { history ->
                if (history.isNotEmpty()) {
                    _messages.value = history
                } else {
                    // Tin nhắn chào mừng từ AI nếu chưa có lịch sử
                    _messages.value = listOf(
                        ChatMessage(
                            content = "Xin chào! Tôi là SET AI 👋\nTôi có thể giúp bạn phân tích chi tiêu, lập kế hoạch ngân sách và đưa ra lời khuyên tài chính. Bạn muốn hỏi gì?",
                            isUser = false,
                            time = currentTime()
                        )
                    )
                }
            }
        }
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return

        // Thêm tin nhắn người dùng
        val userMessage = ChatMessage(content = text, isUser = true, time = currentTime())
        _messages.value = _messages.value + userMessage
        _isLoading.value = true

        viewModelScope.launch {
            val result = sendChatMessageUseCase.execute(text)
            _isLoading.value = false

            val aiMessage = result.fold(
                onSuccess = { reply ->
                    ChatMessage(content = reply, isUser = false, time = currentTime())
                },
                onFailure = {
                    ChatMessage(
                        content = "Xin lỗi, tôi gặp sự cố kết nối. Vui lòng kiểm tra mạng và thử lại.",
                        isUser = false,
                        time = currentTime()
                    )
                }
            )
            _messages.value = _messages.value + aiMessage
        }
    }

    private fun currentTime(): String =
        LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
}
