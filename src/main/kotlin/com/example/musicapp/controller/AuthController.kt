package com.example.musicapp.controller

import com.example.musicapp.model.User
import com.example.musicapp.repository.UserRepository
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
}






