package com.example.musicapp.repository

import com.example.musicapp.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Long> {
    // username을 기준으로 User를 조회하는 메서드 정의
    fun findByUsername(username: String): User?

    // ID를 기준으로 User를 조회하는 메서드를 명시적으로 추가
    override fun findById(id: Long): Optional<User>
}
