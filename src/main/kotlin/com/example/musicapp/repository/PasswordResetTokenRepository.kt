package com.example.musicapp.repository

import com.example.musicapp.model.PasswordResetToken
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface PasswordResetTokenRepository : JpaRepository<PasswordResetToken, Long> {

    // 토큰을 찾는 메서드
    fun findByToken(token: String): PasswordResetToken?

    // createdDate가 특정 시간 이전인 모든 토큰을 조회하는 메서드
    fun findAllByCreatedDateBefore(createdDate: LocalDateTime): List<PasswordResetToken>
}
