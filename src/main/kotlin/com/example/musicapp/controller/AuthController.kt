package com.example.musicapp.controller

import com.example.musicapp.model.User
import com.example.musicapp.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    // 회원가입 처리
    @PostMapping("/register")
    fun register(@RequestBody user: User): ResponseEntity<Map<String, String>> {
        // 중복 사용자 체크
        val existingUser = userRepository.findByUsername(user.username)
        if (existingUser != null) {
            // 이미 존재하는 사용자일 경우 JSON 형식으로 오류 메시지 반환
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                mapOf("message" to "Username already exists")
            )
        }

        // 비밀번호 암호화
        val encryptedPassword = passwordEncoder.encode(user.password)
        user.password = encryptedPassword

        // 사용자 저장
        userRepository.save(user)

        // 성공적으로 저장된 경우
        return ResponseEntity.ok(mapOf("message" to "User registered successfully"))
    }
}







