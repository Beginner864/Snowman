package com.example.musicapp.controller

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.musicapp.model.LoginRequest
import com.example.musicapp.repository.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class LoginController(
    private val userRepository: UserRepository
) {

    @Value("\${jwt.secret}")
    private lateinit var secretKey: String  // JWT를 위한 비밀 키

    // 로그인 요청 처리
    @PostMapping("/auth/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<Any> {
        // 사용자 인증 처리 (DB에서 사용자 확인)
        val user = userRepository.findByUsername(loginRequest.username)
        if (user != null && user.password == loginRequest.password) {
            // JWT 생성
            val token = generateJwtToken(loginRequest.username)

            // 로그인 성공 시 JWT 토큰 반환
            return ResponseEntity.ok(mapOf("token" to token))
        }

        // 로그인 실패 시 401 상태 코드 반환
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패: 잘못된 사용자명 또는 비밀번호")
    }

    // JWT 토큰 생성 함수
    private fun generateJwtToken(username: String): String {
        // JWT 토큰 만료 시간 설정 (1시간)
        val expirationTime = 3600000L // 1시간 (밀리초 단위)

        // JWT 생성
        return JWT.create()
            .withSubject(username)  // 사용자명 (서브젝트)
            .withIssuedAt(Date())  // 발급 시간
            .withExpiresAt(Date(System.currentTimeMillis() + expirationTime))  // 만료 시간 설정
            .sign(Algorithm.HMAC256(secretKey))  // 비밀 키를 사용하여 서명
    }
}

