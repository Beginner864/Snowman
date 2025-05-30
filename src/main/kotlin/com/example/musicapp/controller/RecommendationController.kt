package com.example.musicapp.controller

import com.example.musicapp.model.MoodRequest
import com.example.musicapp.model.SongResponse
import com.example.musicapp.repository.SongRepository
import com.example.musicapp.service.GptService
import com.example.musicapp.service.GptMoodSummarizer
import com.example.musicapp.repository.UserRepository
import com.example.musicapp.security.SecurityUtil
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/recommend")
class RecommendationController(
    private val songRepository: SongRepository,
    private val gptService: GptService,
    private val gptMoodSummarizer: GptMoodSummarizer,
    private val userRepository: UserRepository // <- 추가
) {

    @PostMapping
    fun recommendByMood(
        @RequestBody request: MoodRequest
    ): SongResponse {
        // 로그인된 사용자 ID 가져오기
        val userId = SecurityUtil.getCurrentUserId()
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.")

        // 사용자 정보 조회
        val user = userRepository.findById(userId).orElseThrow {
            ResponseStatusException(HttpStatus.UNAUTHORIZED, "사용자를 찾을 수 없습니다.")
        }

        // 사용자가 저장한 곡들 조회
        val songs = songRepository.findByUserId(userId)
        if (songs.isEmpty()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자가 저장한 곡이 없습니다.")
        }

        // user_songs로 필드명 변경
        val songRequestList: List<Map<String, Any>> = songs.mapNotNull {
            it.id?.let { id -> // id가 null이 아닐 때만 처리
                mapOf(
                    "id" to id.toInt(),
                    "title" to it.title,
                    "artist" to it.artist,
                    "genre" to it.genre,
                    "mood" to it.mood,
                    "streaming_url" to it.streamingUrl
                ).mapValues { it.value as Any }
            }
        }


        // GPT 감정 분석 요청
        val moodKeywords = gptMoodSummarizer.extractKeywordsFromText(request.input)
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "감정 분석 실패")

        // GPT 추천 요청
        val recommended = gptService.requestRecommendation(moodKeywords, songRequestList)
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "추천 서버 응답 없음")

        // 추천된 곡 반환
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









