package com.example.musicapp.security

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User

object SecurityUtil {

    fun getCurrentUserId(): Long {
        val principal = SecurityContextHolder.getContext().authentication.principal
        return (principal as User).username.toLong()  // JWT에서 저장한 username을 사용하여 사용자 ID를 추출
    }
}
