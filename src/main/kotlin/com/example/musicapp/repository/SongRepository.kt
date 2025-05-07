package com.example.musicapp.repository

import com.example.musicapp.model.Song
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface SongRepository : JpaRepository<Song, Long> {

    // mood를 기반으로 노래 조회
    @Query("SELECT s FROM Song s WHERE LOWER(s.mood) = LOWER(:mood)")
    fun findByMood(mood: String): List<Song>

    // userId를 기반으로 노래 조회
    @Query("SELECT s FROM Song s WHERE s.user.id = :userId")
    fun findByUserId(userId: Long): List<Song>

    // 기분과 사용자 ID를 바탕으로 노래 조회
    fun findByMoodAndUserId(mood: String, userId: Long): List<Song>
}




