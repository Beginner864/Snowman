package com.example.musicapp.service

import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.client.RestTemplate
import org.springframework.http.*
import com.fasterxml.jackson.databind.ObjectMapper

@Service
class GptService {

    private val restTemplate = RestTemplate()

    // AppConfig에서 주입된 API Key
    @Autowired
    private lateinit var apiKey: String

    fun extractMoodFromText(input: String): String {
        val url = "https://api.openai.com/v1/chat/completions"
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            setBearerAuth(apiKey)
        }

        val requestJson = """
            {
              "model": "gpt-3.5-turbo",
              "messages": [
                {"role": "system", "content": "당신은 사용자의 기분을 분석하는 도우미입니다."},
                {"role": "user", "content": "다음 문장에서 기분을 한 단어로 영어로 추출해주세요: \"$input\". 결과는 영어로 한 단어만 출력하세요."}
              ]
            }
        """.trimIndent()

        val request = HttpEntity(requestJson, headers)

        val response = restTemplate.exchange(url, HttpMethod.POST, request, String::class.java)
        val mapper = ObjectMapper()
        val rootNode = mapper.readTree(response.body)
        val mood = rootNode["choices"]
            .get(0)
            .get("message")
            .get("content")
            .asText()
            .trim()
            .lowercase()

        return mood
    }
}
