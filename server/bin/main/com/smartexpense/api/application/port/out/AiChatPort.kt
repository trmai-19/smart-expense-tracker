package com.smartexpense.api.application.port.out

interface AiChatPort {
    /**
     * Gửi tin nhắn tới AI và nhận phản hồi.
     * @param userMessage Tin nhắn từ người dùng
     * @param history Lịch sử hội thoại gần nhất (role to content pairs), tối đa 10 lượt
     * @return Phản hồi từ AI
     */
    fun chat(userMessage: String, history: List<Pair<String, String>> = emptyList(), context: String? = null): String
}
