package com.example.musicapp.model

import com.fasterxml.jackson.annotation.JsonProperty

data class SongResponse(
    val id: Long,
    val title: String,
    val artist: String,
    val genre: String,
    val mood: String,
    @JsonProperty("streaming_url")
    val streamingUrl: String
)
