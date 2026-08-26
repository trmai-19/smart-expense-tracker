package com.smartexpense.android.data.remote.api

import com.smartexpense.android.data.remote.dto.request.ChatRequestDto
import com.smartexpense.android.data.remote.dto.response.ChatResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatApi {
    @POST("api/chat/send")
    suspend fun sendMessage(@Body request: ChatRequestDto): ChatResponseDto
}
