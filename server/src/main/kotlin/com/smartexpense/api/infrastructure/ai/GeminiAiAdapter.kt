package com.smartexpense.api.infrastructure.ai

import com.smartexpense.api.application.port.out.AiChatPort
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

/**
 * Adapter gọi Gemini 1.5 Flash API để xử lý hội thoại tư vấn tài chính.
 *
 * Endpoint: POST https://generativelanguage.googleapis.com/v1beta/models/{model}:generateContent
 * Docs: https://ai.google.dev/gemini-api/docs/text-generation
 */
@Component
class GeminiAiAdapter(
    @Value("\${gemini.api-key}") private val apiKey: String,
    @Value("\${gemini.model:gemini-1.5-flash}") private val model: String,
    private val restTemplate: RestTemplate
) : AiChatPort {

    companion object {
        private const val GEMINI_BASE_URL =
            "https://generativelanguage.googleapis.com/v1beta/models"

        private const val SYSTEM_INSTRUCTION = """
Bạn là SET AI — trợ lý tư vấn tài chính thông minh của ứng dụng Smart Expense Tracker (SET).
Nhiệm vụ của bạn:
- Phân tích thói quen chi tiêu và đưa ra lời khuyên cụ thể, thiết thực.
- Giúp người dùng lập kế hoạch ngân sách và tiết kiệm hiệu quả.
- Trả lời ngắn gọn, thân thiện, sử dụng tiếng Việt.
- Không cung cấp thông tin tài chính sai lệch hay lời khuyên đầu tư rủi ro.
"""
    }

    override fun chat(userMessage: String, history: List<Pair<String, String>>): String {
        val url = "$GEMINI_BASE_URL/$model:generateContent?key=$apiKey"

        val contents = history.map { (role, text) ->
            mapOf(
                "role" to role,
                "parts" to listOf(mapOf("text" to text))
            )
        } + mapOf(
            "role" to "user",
            "parts" to listOf(mapOf("text" to userMessage))
        )

        val requestBody = mapOf(
            "system_instruction" to mapOf(
                "parts" to listOf(mapOf("text" to SYSTEM_INSTRUCTION.trim()))
            ),
            "contents" to contents,
            "generationConfig" to mapOf(
                "temperature" to 0.7,
                "maxOutputTokens" to 1024,
                "topP" to 0.95
            )
        )

        return try {
            val headers = HttpHeaders().apply {
                contentType = MediaType.APPLICATION_JSON
            }
            val entity = HttpEntity(requestBody, headers)
            val response = restTemplate.postForObject(url, entity, Map::class.java)

            // Parse response: candidates[0].content.parts[0].text
            @Suppress("UNCHECKED_CAST")
            val candidates = response?.get("candidates") as? List<Map<String, Any>>
            val content = candidates?.firstOrNull()?.get("content") as? Map<String, Any>
            val parts = content?.get("parts") as? List<Map<String, Any>>
            parts?.firstOrNull()?.get("text") as? String
                ?: "Xin lỗi, tôi không thể phản hồi lúc này. Vui lòng thử lại."
        } catch (e: Exception) {
            "Xin lỗi, có lỗi xảy ra khi kết nối AI. Vui lòng thử lại sau."
        }
    }
}
