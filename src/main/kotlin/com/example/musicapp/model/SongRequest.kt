package com.example.musicapp.model

data class SongRequest(
    var title: String,
    var artist: String,
    var genre: String,
    var mood: String,
    var streamingUrl: String,
    var userId: Long // userId는 POST, PUT 요청에서 공통으로 필요
)

