package com.example.musicapp.repository

import com.example.musicapp.model.Song
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query


interface SongRepository : JpaRepository<Song, Long> {
    @Query("SELECT s FROM Song s WHERE LOWER(s.mood) = LOWER(:mood)")
    fun findByMood(mood: String): List<Song>
}



