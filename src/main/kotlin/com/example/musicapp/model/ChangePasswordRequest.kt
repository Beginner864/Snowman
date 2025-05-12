package com.example.musicapp.model

data class ChangePasswordRequest(
    val currentPassword: String,
    val newPassword: String
)

