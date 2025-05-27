package com.example.musicapp.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class GptMoodSummarizer {

    private val restTemplate = RestTemplate()
    private val openAiApiKey = System.getenv("OPENAI_API_KEY")  // 환경 변수로부터 API 키 로드
    private val openAiUrl = "https://api.openai.com/v1/chat/completions"

    fun extractKeywordsFromText(input: String): String? {
        val prompt = """
            Translate the following Korean sentence and summarize the emotional or atmospheric tone in 1 to 3 concise English keywords.
            Input: "$input"
            Output:
        """.trimIndent()

        val body = mapOf(
            "model" to "gpt-3.5-turbo",
            "messages" to listOf(
                mapOf("role" to "user", "content" to prompt)
            ),
            "temperature" to 0.7
        )

        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            set("Authorization", "Bearer $openAiApiKey")
        }

        val request = HttpEntity(body, headers)

        return try {
            val response = restTemplate.postForEntity(openAiUrl, request, String::class.java)
            if (response.statusCode.is2xxSuccessful) {
                val json = ObjectMapper().readTree(response.body)
                json["choices"]?.get(0)?.get("message")?.get("content")?.asText()?.trim()
            } else null
        } catch (e: Exception) {
            println("GPT 호출 실패: ${e.message}")
            null
        }
    }
}
