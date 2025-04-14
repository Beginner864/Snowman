package com.example.musicapp.controller

import com.example.musicapp.model.Song
import com.example.musicapp.repository.SongRepository
import com.example.musicapp.service.GptService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

data class MoodRequest(val input: String)

@RestController
@RequestMapping("/recommend")
class RecommendationController(
    private val songRepository: SongRepository,
    private val gptService: GptService
) {

    @PostMapping
    fun recommendByMood(@RequestBody request: MoodRequest): Song {
        val mood = gptService.extractMoodFromText(request.input)

        if (mood.isNullOrBlank()) {
            // 400 Bad Request: 기분 추출 실패 시
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "유효한 기분을 추출할 수 없습니다.")
        }

        println("추천할 기분: $mood")

        val matchingSongs = songRepository.findByMood(mood)

        println("찾은 노래 수: ${matchingSongs.size}")

        return matchingSongs.randomOrNull()
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "기분에 맞는 노래를 찾을 수 없습니다. 요청한 기분: $mood"
            )
    }
}






