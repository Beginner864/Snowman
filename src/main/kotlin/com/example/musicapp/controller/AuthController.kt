package com.example.musicapp.controller

import com.example.musicapp.model.RegisterRequest
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
    fun register(@RequestBody registerRequest: RegisterRequest): ResponseEntity<Map<String, String>> {
        // 중복 사용자 체크 (이메일과 사용자명을 동시에 체크)
        val existingUserByUsername = userRepository.findByUsername(registerRequest.username)
        val existingUserByEmail = userRepository.findByEmail(registerRequest.email)

        if (existingUserByUsername != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                mapOf("message" to "Username already exists")
            )
        }

        if (existingUserByEmail != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                mapOf("message" to "Email already exists")
            )
        }

        // 비밀번호 암호화
        val encryptedPassword = passwordEncoder.encode(registerRequest.password)

        // RegisterRequest를 User 객체로 변환하여 저장
        val user = User(
            username = registerRequest.username,
            password = encryptedPassword,
            email = registerRequest.email
        )

        // 사용자 저장
        userRepository.save(user)

        // 성공적으로 저장된 경우
        return ResponseEntity.ok(mapOf("message" to "User registered successfully"))
    }
}








