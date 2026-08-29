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
- Trả lời ngắn gọn, thân thiện, sử dụng tiếng Việt. Tuyệt đối KHÔNG sử dụng định dạng Markdown (như **, *, #, v.v.) trong câu trả lời.
- Không cung cấp thông tin tài chính sai lệch hay lời khuyên đầu tư rủi ro.
"""
    }

    override fun chat(userMessage: String, history: List<Pair<String, String>>, context: String?): String {
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

        val finalSystemInstruction = if (context != null) {
            """
$SYSTEM_INSTRUCTION

LUẬT QUAN TRỌNG:
1. Bạn vừa nhận được câu hỏi từ người dùng và dữ liệu chi tiêu của họ ở bên dưới.
2. Nếu câu hỏi KHÔNG LIÊN QUAN ĐẾN: tài chính, chi tiêu, thu nhập, ngân sách, phân tích tài chính hoặc cách sử dụng ứng dụng (Ví dụ các câu hỏi cấm: hỏi về tình yêu, thời tiết, giải toán, kiến thức chung...), BẠN PHẢI TRẢ LỜI DUY NHẤT một từ: "Nothing". Tuyệt đối không giải thích thêm.
3. Nếu câu hỏi hợp lệ (kể cả những lời chào hỏi thông thường như "chào bạn", "hello"), hãy trả lời như một người trợ lý tài chính thân thiện, nhiệt tình bằng tiếng Việt. KHÔNG liệt kê số liệu khô khan như máy móc. Hãy trả lời thành câu hoàn chỉnh, có cảm xúc.

Dữ liệu của người dùng hiện tại:
$context
            """.trim()
        } else {
            SYSTEM_INSTRUCTION.trim()
        }

        val requestBody = mapOf(
            "system_instruction" to mapOf(
                "parts" to listOf(mapOf("text" to finalSystemInstruction))
            ),
            "contents" to contents,
            "generationConfig" to mapOf(
                "temperature" to 0.7,
                "maxOutputTokens" to 4096,
                "topP" to 0.95
            )
        )

        return try {
            val headers = HttpHeaders().apply {
                contentType = MediaType.APPLICATION_JSON
            }
            val entity = HttpEntity(requestBody, headers)
            val response = restTemplate.postForObject(url, entity, Map::class.java)

            // Parse response: candidates[0].content.parts
            @Suppress("UNCHECKED_CAST")
            val candidates = response?.get("candidates") as? List<Map<String, Any>>
            val content = candidates?.firstOrNull()?.get("content") as? Map<String, Any>
            val parts = content?.get("parts") as? List<Map<String, Any>>
            
            val reply = parts?.joinToString("") { it["text"] as? String ?: "" }
            if (reply.isNullOrBlank()) {
                "Xin lỗi, tôi không thể phản hồi lúc này. Vui lòng thử lại."
            } else {
                reply
            }
        } catch (e: Exception) {
            java.io.File("gemini_chat_error.txt").writeText(
                "Error in GeminiAiAdapter: ${e.message}\n" +
                e.stackTraceToString()
            )
            "Xin lỗi, có lỗi xảy ra khi kết nối AI. Vui lòng thử lại sau."
        }
    }
}
