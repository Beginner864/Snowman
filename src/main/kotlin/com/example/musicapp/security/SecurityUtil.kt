package com.example.musicapp.security

import org.springframework.security.core.context.SecurityContextHolder
import com.example.musicapp.security.CustomUserDetails

object SecurityUtil {

    fun getCurrentUserId(): Long {
        val principal = SecurityContextHolder.getContext().authentication.principal
        // principal이 CustomUserDetails 객체라면 getUserId()를 호출
        return (principal as CustomUserDetails).getUserId()
    }
}




