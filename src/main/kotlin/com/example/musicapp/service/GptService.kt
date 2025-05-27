package com.example.musicapp.service

import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import com.fasterxml.jackson.databind.ObjectMapper

@Service
class GptService {

    private val restTemplate = RestTemplate()
    private val pythonServerUrl = "https://songaii.onrender.com"

    fun requestRecommendation(mood: String, songs: List<Map<String, Any>>): Map<String, Any>? {
        val url = "$pythonServerUrl/recommend"

        val body = mapOf(
            "mood" to mood,
            "songs" to songs
        )

        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }

        val requestEntity = HttpEntity(body, headers)

        return try {
            val response = restTemplate.postForEntity(url, requestEntity, String::class.java)
            if (response.statusCode.is2xxSuccessful) {
                val mapper = ObjectMapper()
                mapper.readValue(response.body, Map::class.java) as Map<String, Any>
            } else {
                null
            }
        } catch (e: Exception) {
            println("[GptService] Python 서버 연결 실패: ${e.message}")
            null
        }
    }
}
