package com.smartexpense.android.data.remote.api

import com.smartexpense.android.data.remote.dto.request.ChatRequestDto
import com.smartexpense.android.data.remote.dto.response.ChatResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import com.smartexpense.android.data.remote.dto.response.ChatMessageDto

interface ChatApi {
    @POST("api/chat/send")
    suspend fun sendMessage(@Body request: ChatRequestDto): ChatResponseDto

    @GET("api/chat/history")
    suspend fun getChatHistory(): List<ChatMessageDto>
}
