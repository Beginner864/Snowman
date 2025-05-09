package com.example.musicapp.service

import com.example.musicapp.model.PasswordResetToken
import com.example.musicapp.model.User
import com.example.musicapp.repository.PasswordResetTokenRepository
import com.example.musicapp.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class PasswordResetService(
    @Autowired private val passwordResetTokenRepository: PasswordResetTokenRepository,
    @Autowired private val userRepository: UserRepository
) {

    // 비밀번호 재설정 토큰 생성
    fun createPasswordResetToken(user: User): String {
        // 고유한 토큰 생성
        val token = UUID.randomUUID().toString()  // UUID를 사용하여 고유 토큰 생성

        // 생성된 토큰을 PasswordResetToken 테이블에 저장
        val passwordResetToken = PasswordResetToken(
            user = user,
            token = token,
            createdDate = LocalDateTime.now()  // 현재 시간 기록
        )
        passwordResetTokenRepository.save(passwordResetToken)

        return token  // 생성된 토큰 반환
    }


    // 비밀번호 재설정
    fun resetPassword(token: String, newPassword: String): String {
        val passwordResetToken = validateResetToken(token)
            ?: return "Invalid or expired token."

        val user = passwordResetToken.user
        user.password = BCryptPasswordEncoder().encode(newPassword)  // 새로운 비밀번호 암호화
        userRepository.save(user)  // 비밀번호 업데이트

        return "Password has been successfully reset."
    }

    // 토큰 유효성 검증
    fun validateResetToken(token: String): PasswordResetToken? {
        val passwordResetToken = passwordResetTokenRepository.findByToken(token)
        return if (passwordResetToken != null && !isTokenExpired(passwordResetToken)) {
            passwordResetToken
        } else {
            null
        }
    }

    // 토큰 만료 여부 확인
    fun isTokenExpired(token: PasswordResetToken): Boolean {
        val expirationTime = token.createdDate.plusMinutes(30)  // 예: 30분 후 만료
        return expirationTime.isBefore(LocalDateTime.now())  // 만료 여부 확인
    }

    // 주기적으로 만료된 토큰 삭제
    @Scheduled(fixedRate = 3600000)  // 1시간(3600000ms)마다 실행
    fun deleteExpiredTokens() {
        val expirationTime = LocalDateTime.now().minusMinutes(30)  // 30분 전에 생성된 토큰 삭제
        val expiredTokens = passwordResetTokenRepository.findAllByCreatedDateBefore(expirationTime)
        expiredTokens.forEach { passwordResetTokenRepository.delete(it) }
    }
}



