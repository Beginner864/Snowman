package com.example.musicapp.model

import jakarta.persistence.*

@Entity
@Table(name = "users")  // 테이블 이름을 users로 변경
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val username: String,
    var password: String,
)

