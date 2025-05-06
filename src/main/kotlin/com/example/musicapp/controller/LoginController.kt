package com.example.musicapp.controller

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.musicapp.model.LoginRequest
import com.example.musicapp.repository.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class LoginController(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder  // 비밀번호 검증을 위한 BCryptPasswordEncoder
) {

    @Value("\${jwt.secret}")
    private lateinit var secretKey: String  // JWT 비밀 키

    // 로그인 요청 처리
    @PostMapping("/auth/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<Any> {
        // 사용자 인증 처리 (DB에서 사용자 확인)
        val user = userRepository.findByUsername(loginRequest.username)
        if (user != null) {
            // 비밀번호 암호화된 것과 입력한 비밀번호를 비교
            if (passwordEncoder.matches(loginRequest.password, user.password)) {
                // JWT 생성
                val token = generateJwtToken(loginRequest.username)

                // 로그인 성공 시 JWT 토큰 반환
                return ResponseEntity.ok(mapOf("token" to token))
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password")
        }

        // 사용자명이 존재하지 않으면 로그인 실패
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found")
    }

    // JWT 토큰 생성 함수
    private fun generateJwtToken(username: String): String {
        val expirationTime = 3600000L // 1시간 (밀리초 단위)

        return JWT.create()
            .withSubject(username)  // 사용자명 (서브젝트)
            .withIssuedAt(Date())  // 발급 시간
            .withExpiresAt(Date(System.currentTimeMillis() + expirationTime))  // 만료 시간 설정
            .sign(Algorithm.HMAC256(secretKey))  // 비밀 키를 사용하여 서명
    }
}


