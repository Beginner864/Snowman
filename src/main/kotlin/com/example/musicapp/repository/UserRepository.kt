package com.example.musicapp.repository

import com.example.musicapp.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    // username을 기준으로 User를 조회하는 메서드 정의
    fun findByUsername(username: String): User?
}
