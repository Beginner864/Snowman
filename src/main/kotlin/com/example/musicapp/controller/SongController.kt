package com.example.musicapp.controller

import com.example.musicapp.model.Song
import com.example.musicapp.repository.SongRepository
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/songs")
class SongController(
    private val songRepository: SongRepository,

) {

    @PostMapping
    fun addSong(@RequestBody song: Song): Song {
        // Song 생성 시 user는 필수이므로, user가 null일 경우 예외 처리
        song.user?.let {
            return songRepository.save(song)
        } ?: throw IllegalArgumentException("User must be provided")
    }

    @GetMapping
    fun getAllSongs(): List<Song> {
        return songRepository.findAll()
    }

    @GetMapping("/mood/{mood}")
    fun getSongsByMood(@PathVariable mood: String): List<Song> {
        return songRepository.findByMood(mood)
    }

    @PutMapping("/{id}")
    fun updateSong(@PathVariable id: Long, @RequestBody song: Song): Song {
        // 기존 노래 조회
        val existingSong = songRepository.findById(id).orElseThrow { RuntimeException("Song not found") }

        // 노래 정보 수정
        existingSong.title = song.title
        existingSong.artist = song.artist
        existingSong.genre = song.genre
        existingSong.mood = song.mood
        existingSong.streamingUrl = song.streamingUrl
        existingSong.user = song.user // user 업데이트

        // 수정된 노래 저장
        return songRepository.save(existingSong)
    }

    @DeleteMapping("/{id}")
    fun deleteSong(@PathVariable id: Long) {
        songRepository.deleteById(id)
    }
}



