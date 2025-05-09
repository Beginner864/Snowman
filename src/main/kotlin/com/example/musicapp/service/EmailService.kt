package com.example.musicapp.service

import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.mail.MailException
import org.springframework.stereotype.Service

@Service
class EmailService(private val javaMailSender: JavaMailSender) {

    // 비밀번호 재설정 이메일 전송
    fun sendPasswordResetEmail(to: String, token: String) {
        val resetUrl = "http://your-app.com/reset-password?token=$token"  // 비밀번호 재설정 링크
        val subject = "Password Reset Request"
        val message = """
            <h3>Password Reset Request</h3>
            <p>Click the link below to reset your password:</p>
            <a href="$resetUrl">$resetUrl</a>
        """
        sendHtmlEmail(to, subject, message)
    }

    // HTML 형식 이메일 보내기
    fun sendHtmlEmail(to: String, subject: String, message: String) {
        try {
            val mimeMessage = javaMailSender.createMimeMessage()
            val helper = MimeMessageHelper(mimeMessage, true)
            helper.setTo(to)
            helper.setSubject(subject)
            helper.setText(message, true)  // true로 설정하면 HTML 형식으로 전송

            javaMailSender.send(mimeMessage)
        } catch (e: MailException) {
            e.printStackTrace()
        }
    }
}






