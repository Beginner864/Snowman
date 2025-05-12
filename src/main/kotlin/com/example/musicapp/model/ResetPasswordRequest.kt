package com.example.musicapp.model

data class ResetPasswordRequest(
    val token: String,
    val newPassword: String
)

