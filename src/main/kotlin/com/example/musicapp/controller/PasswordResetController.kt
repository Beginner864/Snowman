package com.example.musicapp.controller

import com.example.musicapp.model.ChangePasswordRequest
import com.example.musicapp.model.ResetPasswordRequest
import com.example.musicapp.model.ResponseMessage
import com.example.musicapp.security.SecurityUtil
import com.example.musicapp.service.PasswordResetService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/auth")
class PasswordResetController(
    @Autowired private val passwordResetService: PasswordResetService
) {

    // 비밀번호를 잊었을 때 비밀번호 변경
    @PostMapping("/reset-password")
    fun resetPassword(@RequestBody request: ResetPasswordRequest): ResponseEntity<ResponseMessage> {
        val result = passwordResetService.resetPassword(request.token, request.newPassword)

        return if (result == "Password has been successfully reset.") {
            ResponseEntity.ok(ResponseMessage("success", result))
        } else {
            ResponseEntity.badRequest().body(ResponseMessage("error", result))
        }
    }


    // 로그인한 사용자가 비밀번호 변경
    @PostMapping("/change-password")
    fun changePassword(@RequestBody request: ChangePasswordRequest): ResponseEntity<ResponseMessage> {
        return try {
            val userId = SecurityUtil.getCurrentUserId()
            val response = passwordResetService.changePassword(userId, request.currentPassword, request.newPassword)
            ResponseEntity.ok(response)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(ResponseMessage("error", e.message ?: "입력 오류"))
        } catch (e: Exception) {
            ResponseEntity.internalServerError().body(ResponseMessage("error", "서버 오류가 발생했습니다."))
        }
    }

}
