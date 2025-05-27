package com.example.musicapp.service

import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.http.*
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value

@Service
class GptService {

    private val restTemplate = RestTemplate()

    // 추천 서버 주소 (Render 도메인으로 변경)
    private val pythonServerUrl = "https://songaii.onrender.com"

    fun extractMoodFromText(input: String): Map<String, Any>? {
        val url = "$pythonServerUrl/recommend?mood=${input.trim()}"

        return try {
            val response = restTemplate.getForEntity(url, String::class.java)
            if (response.statusCode.is2xxSuccessful) {
                val mapper = ObjectMapper()
                mapper.readValue(response.body, Map::class.java) as Map<String, Any>
            } else {
                null
            }
        } catch (e: Exception) {
            println("Python 서버 연결 실패: ${e.message}")
            null
        }
    }
}
