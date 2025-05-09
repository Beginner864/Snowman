package com.example.musicapp.service

import com.example.musicapp.model.User
import com.example.musicapp.repository.UserRepository
import com.example.musicapp.security.SecurityUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Service
class UserService @Autowired constructor(
    private val userRepository: UserRepository,
    @Autowired private val passwordResetService: PasswordResetService,
    @Autowired private val emailService: EmailService
) {

    private val passwordEncoder = BCryptPasswordEncoder() // 암호화된 비밀번호 비교를 위한 인코더

    @Transactional
    fun deleteUser(password: String): String {
        // 현재 로그인한 사용자의 ID를 가져옵니다.
        val currentUserId = SecurityUtil.getCurrentUserId()

        // 해당 사용자가 존재하는지 확인
        val user = userRepository.findById(currentUserId)
            .orElseThrow { RuntimeException("User not found") }

        // 비밀번호 확인 (암호화된 비밀번호 비교)
        if (passwordEncoder.matches(password, user.password)) {
            // 비밀번호가 일치하는 경우 사용자 삭제
            userRepository.delete(user)
            return "회원탈퇴가 완료되었습니다."
        } else {
            println("비밀번호 비교 실패: 입력된 비밀번호 = $password, 저장된 비밀번호 = ${user.password}")
            throw RuntimeException("Incorrect password")
        }
    }

    // 이메일로 사용자 조회 (아이디 찾기 기능)
    fun findUsernameByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }


    // 사용자 이메일로 비밀번호 재설정 링크 전송
    fun sendPasswordResetLinkToEmail(username: String): String {
        val user = userRepository.findByUsername(username)
            ?: return "User not found"  // 사용자가 없으면 에러 메시지 반환

        // 비밀번호 재설정 토큰 생성
        val token = passwordResetService.createPasswordResetToken(user)

        // 이메일로 비밀번호 재설정 링크 전송
        emailService.sendPasswordResetEmail(user.email, token)

        return "Password reset link has been sent to your email."
    }
}



