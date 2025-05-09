package com.example.musicapp.model

data class SongRequest(
    var title: String,
    var artist: String,
    var genre: String,
    var mood: String,
    var streamingUrl: String,
)

