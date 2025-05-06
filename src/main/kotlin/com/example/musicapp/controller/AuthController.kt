package com.example.musicapp.controller

import com.example.musicapp.model.LoginRequest
import com.example.musicapp.model.User
import com.example.musicapp.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(
    @Autowired private val userRepository: UserRepository
) {

    @PostMapping("/register")
    fun register(@RequestBody user: User): String {
        val existingUser = userRepository.findByUsername(user.username)
        if (existingUser != null) {
            return "Username already exists"
        }
        userRepository.save(user)
        return "User registered successfully"
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): String {
        // 로그인 로직 처리 (Spring Security에서 처리됨)
        return "Logged in successfully"
    }
}

