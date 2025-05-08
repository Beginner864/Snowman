package com.example.musicapp.repository

import com.example.musicapp.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    // JpaRepository에 기본적으로 findById가 제공됨
}
