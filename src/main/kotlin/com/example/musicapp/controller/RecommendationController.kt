package com.example.musicapp.controller

import com.example.musicapp.model.MoodRequest
import com.example.musicapp.model.Song
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
        // GPT 서비스에서 기분 추출
        val extractedMood = gptService.extractMoodFromText(request.input)

        if (extractedMood.isEmpty()) {
            // 400 Bad Request: 기분 추출 실패 시
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "유효한 기분을 추출할 수 없습니다.")
        }

        println("추천할 기분: $extractedMood")

        // 기분을 정리하여 비교
        val normalizedMood = normalizeMood(extractedMood)

        // 정리된 기분에 맞는 노래를 찾기
        val matchingSongs = songRepository.findAll().filter { song ->
            normalizeMood(song.mood) == normalizedMood // 기분 비교
        }

        println("찾은 노래 수: ${matchingSongs.size}")

        return matchingSongs.randomOrNull()
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "기분에 맞는 노래를 찾을 수 없습니다. 요청한 기분: $extractedMood"
            )
    }
}







