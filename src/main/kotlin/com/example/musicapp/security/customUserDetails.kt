package com.example.musicapp.security

import org.springframework.security.core.userdetails.User
import com.example.musicapp.model.User as AppUser
import org.springframework.security.core.authority.SimpleGrantedAuthority

class CustomUserDetails(private val appUser: AppUser) : User(
    appUser.username, appUser.password, listOf(SimpleGrantedAuthority("ROLE_USER"))
) {
    // 로그인한 사용자의 ID를 반환하는 메소드
    fun getUserId(): Long {
        return appUser.id ?: throw RuntimeException("User ID not found")
    }
}
