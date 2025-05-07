package com.example.musicapp.service

import com.example.musicapp.model.User
import com.example.musicapp.repository.UserRepository
import com.example.musicapp.security.CustomUserDetails
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(private val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        // User가 존재하지 않으면 예외 발생
        val appUser = userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("User not found")

        return CustomUserDetails(appUser)  // CustomUserDetails 객체 반환
    }
}



