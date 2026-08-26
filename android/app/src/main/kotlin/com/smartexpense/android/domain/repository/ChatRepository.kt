package com.smartexpense.android.domain.repository

interface ChatRepository {
    suspend fun sendMessage(message: String): Result<String>
}
