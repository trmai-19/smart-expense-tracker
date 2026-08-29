package com.smartexpense.api.infrastructure.ai

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import java.util.Base64

@Service
class GeminiClient(
    @Value("\${gemini.api-key:}")
    private val apiKey: String
) {
    private val restTemplate = RestTemplate()
    private val apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.6-flash:generateContent?key="

    fun analyzeReceipt(imageBytes: ByteArray, mimeType: String, caption: String?): String {
        if (apiKey.isBlank()) {
            // For testing/fallback if no key is provided
            return "{\"amount\": 0, \"category\": \"Khác\"}"
        }

        val base64Image = Base64.getEncoder().encodeToString(imageBytes)
        
        var prompt = """
            You are an AI assistant that extracts expense information from a receipt image and user caption.
            Extract the total amount and classify the category. 
            The category should be a broad, general expense category in Vietnamese (e.g., 'Ăn uống', 'Di chuyển', 'Mua sắm', 'Giải trí', 'Hóa đơn', 'Sức khỏe', 'Mỹ phẩm', 'Giáo dục'). Avoid overly specific items (for example, use 'Ăn uống' instead of 'Cà phê' or 'Trà sữa', use 'Mỹ phẩm' instead of 'Son môi'). Do not restrict strictly to a predefined list, but keep it at a high, general level.
            If the user provides a caption, analyze it deeply to extract the total expense amount in VND. Users often use Vietnamese slang, misspellings, or abbreviations. Apply this reasoning flexibly:
            - Multipliers: 'k', 'cành', 'cá' = * 1,000; 'chục' = * 10,000; 'lít', 'loét' = * 100,000; 'củ', 'triệu', 'trịu', 'm' = * 1,000,000; 'tỏi' = * 1,000,000,000.
            - Decimals/Fractions: 'rưỡi' adds half of the previous unit (e.g. '2 củ rưỡi' = 2,500,000).
            - Continuations: 'một trịu 2' = 1,200,000; 'hai trăm năm chục' = 250,000.
            Use your natural language understanding to interpret any variation or misspelling of these slangs contextually. Always prioritize the amount mentioned in the caption over the image. If the caption hints at the item, use it to accurately categorize (e.g. lipstick -> 'Mỹ phẩm').
            Return ONLY a valid JSON object without markdown formatting, with keys 'amount' (number) and 'category' (string).
            For example: {"amount": 50000, "category": "Mỹ phẩm"}
        """.trimIndent()
        
        if (!caption.isNullOrBlank()) {
            prompt += "\nUser provided caption: $caption. Take this into account if the receipt is unclear."
        }

        val requestBody = GeminiRequest(
            contents = listOf(
                GeminiContent(
                    parts = listOf(
                        GeminiPartText(text = prompt),
                        GeminiPartInlineData(
                            inlineData = GeminiInlineData(
                                mimeType = mimeType,
                                data = base64Image
                            )
                        )
                    )
                )
            )
        )

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val requestEntity = HttpEntity(requestBody, headers)

        try {
            val response: ResponseEntity<GeminiResponse> = restTemplate.postForEntity(
                "$apiUrl$apiKey",
                requestEntity,
                GeminiResponse::class.java
            )
            
            val responseText = response.body?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            java.io.File("gemini_response.txt").writeText(responseText ?: "null")
            
            val jsonRegex = "\\{.*\\}".toRegex(RegexOption.DOT_MATCHES_ALL)
            val match = responseText?.let { jsonRegex.find(it)?.value }
            
            return match ?: "{\"amount\": 0, \"category\": \"Khác\"}"
        } catch (e: Exception) {
            e.printStackTrace()
            java.io.File("gemini_error.txt").writeText(e.message + "\n" + e.stackTraceToString())
            return "{\"amount\": 0, \"category\": \"Khác\"}"
        }
    }
}

// Request DTOs
data class GeminiRequest(val contents: List<GeminiContent>)
data class GeminiContent(val parts: List<Any>)
data class GeminiPartText(val text: String)
data class GeminiPartInlineData(val inlineData: GeminiInlineData)
data class GeminiInlineData(val mimeType: String, val data: String)

// Response DTOs
@JsonIgnoreProperties(ignoreUnknown = true)
data class GeminiResponse(val candidates: List<GeminiCandidate>?)

@JsonIgnoreProperties(ignoreUnknown = true)
data class GeminiCandidate(val content: GeminiMessageContent?)

@JsonIgnoreProperties(ignoreUnknown = true)
data class GeminiMessageContent(val parts: List<GeminiResponsePart>?)

@JsonIgnoreProperties(ignoreUnknown = true)
data class GeminiResponsePart(val text: String?)
