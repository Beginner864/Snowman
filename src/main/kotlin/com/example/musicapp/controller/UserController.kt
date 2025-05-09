package com.example.musicapp.controller

import com.example.musicapp.model.ResponseMessage
import com.example.musicapp.repository.UserRepository
import com.example.musicapp.security.SecurityUtil
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class UserController(private val userRepository: UserRepository) {

    @DeleteMapping("/delete")
    fun deleteAccount(): ResponseEntity<ResponseMessage> {
        // 현재 로그인한 사용자의 ID를 가져옵니다.
        val currentUserId = SecurityUtil.getCurrentUserId()

        // 해당 사용자가 존재하는지 확인
        val user = userRepository.findById(currentUserId)
            .orElseThrow { RuntimeException("User not found") }

        // 사용자 삭제
        userRepository.delete(user)

        // 응답 객체 반환 (회원탈퇴 완료 메시지와 상태)
        val response = ResponseMessage(
            status = "success",
            message = "회원탈퇴가 완료되었습니다."
        )

        return ResponseEntity(response, HttpStatus.OK)  // HTTP 200 상태 코드와 함께 응답
    }
}

