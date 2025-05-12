package com.example.musicapp.controller

import com.example.musicapp.model.FindIdRequest
import com.example.musicapp.model.FindPWRequest
import com.example.musicapp.model.ResignRequest
import com.example.musicapp.model.ResponseMessage
import com.example.musicapp.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/auth")
class UserController(private val userService: UserService) {

    // 아이디 찾기 기능
    @PostMapping("/find-username")
    fun findUsername(@RequestBody findIdRequest: FindIdRequest): ResponseEntity<ResponseMessage> {
        val user = userService.findUsernameByEmail(findIdRequest.email)

        return if (user != null) {
            val response = ResponseMessage(status = "success", message = "Your username is: ${user.username}")
            ResponseEntity(response, HttpStatus.OK)
        } else {
            val response = ResponseMessage(status = "error", message = "Email not found")
            ResponseEntity(response, HttpStatus.BAD_REQUEST)
        }
    }

    // 비밀번호 찾기 기능
    @PostMapping("/forgot-password")
    fun forgotPassword(@RequestBody findPWRequest: FindPWRequest): ResponseEntity<ResponseMessage> {
        return try {
            val result = userService.sendPasswordResetLinkToEmail(
                findPWRequest.username,
                findPWRequest.email
            )
            ResponseEntity.ok(ResponseMessage("success", result))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(ResponseMessage("error", e.message ?: "Invalid input"))
        } catch (e: Exception) {
            ResponseEntity.internalServerError().body(ResponseMessage("error", "Unexpected error occurred"))
        }
    }


    // 비밀번호를 ResignRequest 객체로 받음
    @PostMapping("/delete") // 원래는 @DeleteMapping이 였다.. (앱에서 @DELETE는 본문 추가가 안 되네..)
    fun deleteAccount(@RequestBody resignRequest: ResignRequest): ResponseEntity<ResponseMessage> {
        return try {
            // 회원탈퇴 처리
            val message = userService.deleteUser(resignRequest.password)

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






