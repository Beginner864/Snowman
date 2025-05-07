package com.example.musicapp.controller

import com.example.musicapp.model.Song
import com.example.musicapp.repository.SongRepository
import com.example.musicapp.repository.UserRepository
import com.example.musicapp.security.SecurityUtil
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/songs")
class SongController(
    private val songRepository: SongRepository,
    private val userRepository: UserRepository
) {

    @PostMapping
    fun addSong(@RequestBody song: Song): Song {
        // 로그인한 사용자 ID를 가져오기
        val currentUserId = SecurityUtil.getCurrentUserId()

        // 현재 사용자의 정보를 UserRepository에서 가져오기
        val user = userRepository.findById(currentUserId)
            .orElseThrow { RuntimeException("User not found") }  // 사용자 찾기 실패 시 예외 발생

        // Song 객체에 user 정보 할당
        song.user = user

        // Song 저장
        return songRepository.save(song)
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
    fun updateSong(@PathVariable id: Long, @RequestBody song: Song): Song {
        val existingSong = songRepository.findById(id).orElseThrow { RuntimeException("Song not found") }

        existingSong.title = song.title
        existingSong.artist = song.artist
        existingSong.genre = song.genre
        existingSong.mood = song.mood
        existingSong.streamingUrl = song.streamingUrl
        existingSong.user = song.user  // user 업데이트

        return songRepository.save(existingSong)
    }

    @DeleteMapping("/{id}")
    fun deleteSong(@PathVariable id: Long) {
        songRepository.deleteById(id)
    }
}







