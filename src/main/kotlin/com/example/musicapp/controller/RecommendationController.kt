package com.example.musicapp.controller

import com.example.musicapp.model.MoodRequest
import com.example.musicapp.model.SongResponse
import com.example.musicapp.service.GptService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/recommend")
class RecommendationController(
    private val gptService: GptService
) {

    @PostMapping
    fun recommendByMood(@RequestBody request: MoodRequest): SongResponse {
        val recommended = gptService.extractMoodFromText(request.input)
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "추천 서버로부터 응답을 받을 수 없습니다.")

        return SongResponse(
            id = (recommended["id"] as Int).toLong(),
            title = recommended["title"] as String,
            genre = recommended["genre"] as String,
            artist = recommended["artist"] as String,
            mood = recommended["mood"] as String,
            streamingUrl = recommended["streaming_url"] as String
        )
    }
}








