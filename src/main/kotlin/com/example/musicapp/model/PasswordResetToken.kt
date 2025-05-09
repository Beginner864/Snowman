package com.example.musicapp.model

import jakarta.persistence.*
import java.time.LocalDateTime  // LocalDateTime을 사용

@Entity
@Table(name = "password_reset_tokens")
data class PasswordResetToken(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,

    val token: String,

    val createdDate: LocalDateTime = LocalDateTime.now()  // LocalDateTime으로 변경
)


