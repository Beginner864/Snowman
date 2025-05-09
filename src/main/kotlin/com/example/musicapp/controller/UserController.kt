package com.example.musicapp.controller

import com.example.musicapp.model.ResponseMessage
import com.example.musicapp.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class UserController(private val userService: UserService) {

    // 비밀번호를 본문에서 받음
    @DeleteMapping("/delete")
    fun deleteAccount(@RequestBody password: String): ResponseEntity<ResponseMessage> {
        return try {
            // 회원탈퇴 처리
            val message = userService.deleteUser(password)

            // 성공 메시지 반환
            val response = ResponseMessage(status = "success", message = message)
            ResponseEntity(response, HttpStatus.OK) // ResponseEntity가 UserService에서 반환한 메시지를 JSON으로 반환
        } catch (e: RuntimeException) {
            // 비밀번호가 일치하지 않거나 오류 발생 시 처리
            val response = ResponseMessage(status = "error", message = e.message ?: "회원탈퇴 실패")
            ResponseEntity(response, HttpStatus.BAD_REQUEST)
        }
    }
}





