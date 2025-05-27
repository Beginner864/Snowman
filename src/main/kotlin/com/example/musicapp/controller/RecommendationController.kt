package com.example.musicapp.controller

import com.example.musicapp.model.MoodRequest
import com.example.musicapp.model.Song
import com.example.musicapp.model.User
import com.example.musicapp.repository.SongRepository
import com.example.musicapp.service.GptService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException


@RestController
@RequestMapping("/recommend")
class RecommendationController(
    private val songRepository: SongRepository,
    private val gptService: GptService
) {

    // 기분을 정리하는 함수
    fun normalizeMood(mood: String): String {
        return mood.trim().removeSuffix(".").removeSuffix("!").lowercase() // 마침표 및 느낌표 제거
    }

    @PostMapping
    fun recommendByMood(@RequestBody request: MoodRequest): Song {
        val recommended = gptService.extractMoodFromText(request.input)

        if (recommended == null) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "추천 서버로부터 응답을 받을 수 없습니다.")
        }

        return Song(
            id = (recommended["id"] as Int).toLong(),
            title = recommended["title"] as String,
            genre = recommended["genre"] as String,
            artist = recommended["artist"] as String,
            mood = recommended["mood"] as String,
            streamingUrl = recommended["streamingUrl"] as String,
            user = recommended["user"] as User
        )
    }

}







