// RecommendationController.kt
package com.example.musicapp.controller

import com.example.musicapp.model.MoodRequest
import com.example.musicapp.model.SongResponse
import com.example.musicapp.repository.SongRepository
import com.example.musicapp.service.GptService
import com.example.musicapp.service.GptMoodSummarizer
import com.example.musicapp.model.User
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/recommend")
class RecommendationController(
    private val songRepository: SongRepository,
    private val gptService: GptService,
    private val gptMoodSummarizer: GptMoodSummarizer
) {

    @PostMapping
    fun recommendByMood(
        @RequestBody request: MoodRequest,
        @AuthenticationPrincipal user: User?
    ): SongResponse {
        if (user == null) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.")
        }

        val userId = user.id ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "사용자 ID가 없습니다.")
        val songs = songRepository.findByUserId(userId)

        if (songs.isEmpty()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자가 저장한 곡이 없습니다.")
        }

        val songRequestList = songs.map {
            mapOf(
                "id" to it.id,
                "title" to it.title,
                "artist" to it.artist,
                "genre" to it.genre,
                "mood" to it.mood,
                "streaming_url" to it.streamingUrl
            ) as Map<String, Any>
        }

        val moodKeywords = gptMoodSummarizer.extractKeywordsFromText(request.input)
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "감정 분석 실패")

        val recommended = gptService.requestRecommendation(moodKeywords, songRequestList)
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "추천 서버 응답 없음")

        return SongResponse(
            id = (recommended["id"] as Int).toLong(),
            title = recommended["title"] as String,
            artist = recommended["artist"] as String,
            genre = recommended["genre"] as String,
            mood = recommended["mood"] as String,
            streamingUrl = recommended["streaming_url"] as String
        )
    }
}






