package com.example.musicapp.controller

import com.example.musicapp.model.Song
import com.example.musicapp.model.SongRequest
import com.example.musicapp.repository.SongRepository
import com.example.musicapp.repository.UserRepository
import com.example.musicapp.security.SecurityUtil
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/songs")
class SongController(
    private val songRepository: SongRepository,
    private val userRepository: UserRepository
) {

    @PostMapping("/songs")
    fun addSong(@RequestBody songRequest: SongRequest): ResponseEntity<Song> {
        // 유효한 사용자 찾기
        val user = userRepository.findById(songRequest.userId)
            .orElseThrow { RuntimeException("User not found") }

        // Song 객체 생성
        val song = Song(
            title = songRequest.title,
            artist = songRequest.artist,
            genre = songRequest.genre,
            mood = songRequest.mood,
            streamingUrl = songRequest.streamingUrl,
            user = user // 사용자 연결
        )

        // 저장
        val savedSong = songRepository.save(song)
        return ResponseEntity.ok(savedSong)
    }


    @GetMapping
    fun getAllSongs(): List<Song> {
        val currentUserId = SecurityUtil.getCurrentUserId()
        return songRepository.findByUserId(currentUserId)  // 로그인한 사용자의 노래만 조회
    }

    @GetMapping("/mood/{mood}")
    fun getSongsByMood(@PathVariable mood: String): List<Song> {
        val currentUserId = SecurityUtil.getCurrentUserId()
        return songRepository.findByMoodAndUserId(mood, currentUserId)  // 사용자별 기분에 맞는 노래만 조회
    }

    @PutMapping("/{id}")
    fun updateSong(@PathVariable id: Long, @RequestBody songRequest: SongRequest): ResponseEntity<Song> {
        // 기존 노래 찾기
        val existingSong = songRepository.findById(id)
            .orElseThrow { RuntimeException("Song not found") }

        // 기존 노래 정보 수정
        existingSong.title = songRequest.title
        existingSong.artist = songRequest.artist
        existingSong.genre = songRequest.genre
        existingSong.mood = songRequest.mood
        existingSong.streamingUrl = songRequest.streamingUrl

        // 수정된 Song 저장
        val updatedSong = songRepository.save(existingSong)
        return ResponseEntity.ok(updatedSong)
    }



    @DeleteMapping("/{id}")
    fun deleteSong(@PathVariable id: Long) {
        songRepository.deleteById(id)
    }
}








