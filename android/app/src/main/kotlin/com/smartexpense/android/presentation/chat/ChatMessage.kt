package com.smartexpense.android.presentation.chat

data class ChatMessage(
    val content: String,
    val isUser: Boolean,
    val time: String
)
