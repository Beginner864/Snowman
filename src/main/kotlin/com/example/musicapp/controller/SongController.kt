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

        // currentUserId가 유효한지 확인하기
        println("Current User ID: $currentUserId")

        // UserRepository에서 User 객체를 ID로 조회
        val user = userRepository.findById(currentUserId)
            .orElseThrow { RuntimeException("User not found") }  // User가 없으면 예외 발생

        // Song 객체에 실제 User 정보 할당
        song.user = user  // User 객체를 할당하면 user_id가 자동으로 설정됨

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

        // 사용자 정보는 클라이언트에서 전달하지 않더라도 서버에서 자동으로 설정
        val currentUserId = SecurityUtil.getCurrentUserId()
        val user = userRepository.findById(currentUserId)
            .orElseThrow { RuntimeException("User not found") }

        // Song 객체에 user 정보 할당
        existingSong.user = user

        // 나머지 필드 업데이트
        existingSong.title = song.title
        existingSong.artist = song.artist
        existingSong.genre = song.genre
        existingSong.mood = song.mood
        existingSong.streamingUrl = song.streamingUrl

        return songRepository.save(existingSong)
    }

    @DeleteMapping("/{id}")
    fun deleteSong(@PathVariable id: Long) {
        songRepository.deleteById(id)
    }
}








