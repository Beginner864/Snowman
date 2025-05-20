package com.example.musicapp.controller

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.musicapp.model.LoginRequest
import com.example.musicapp.model.ResponseMessage
import com.example.musicapp.repository.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class LoginController(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder // 비밀번호 검증을 위한 BCryptPasswordEncoder
) {

    @Value("\${jwt.secret}")
    private lateinit var secretKey: String  // JWT 비밀 키

    // 로그인 요청 처리
    @PostMapping("/auth/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<Any> {
        val user = userRepository.findByUsername(loginRequest.username)

        return if (user != null && passwordEncoder.matches(loginRequest.password, user.password)) {
            val token = generateJwtToken(loginRequest.username)
            ResponseEntity.ok(mapOf("status" to "success", "token" to token))
        } else if (user == null) {
            ResponseEntity.badRequest().body(ResponseMessage("error", "The user with the provided username does not exist."))
        } else {
            ResponseEntity.badRequest().body(ResponseMessage("error", "The password you entered is incorrect."))
        }
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



