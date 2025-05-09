package com.example.musicapp.controller

import com.example.musicapp.service.PasswordResetService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/auth")
class PasswordResetController(
    @Autowired private val passwordResetService: PasswordResetService
) {

    @PostMapping("/reset-password")
    fun resetPassword(@RequestParam token: String, @RequestParam newPassword: String): ResponseEntity<String> {
        val result = passwordResetService.resetPassword(token, newPassword)
        return ResponseEntity.ok(result)
    }
}
