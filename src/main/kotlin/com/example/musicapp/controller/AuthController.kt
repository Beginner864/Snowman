package com.example.musicapp.controller

import com.example.musicapp.model.LoginRequest
import com.example.musicapp.model.User
import com.example.musicapp.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val passwordEncoder: BCryptPasswordEncoder  // 비밀번호 암호화 추가
) {

    @PostMapping("/register")
    fun register(@RequestBody user: User): String {
        // 중복 사용자 체크
        val existingUser = userRepository.findByUsername(user.username)
        if (existingUser != null) {
            return "Username already exists"
        }

        // 비밀번호 암호화
        val encryptedPassword = passwordEncoder.encode(user.password)
        user.password = encryptedPassword

        // 사용자 저장
        userRepository.save(user)

        return "User registered successfully"
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): String {
        // 로그인 로직 처리 (Spring Security에서 처리됨)
        return "Logged in successfully"
    }
}



