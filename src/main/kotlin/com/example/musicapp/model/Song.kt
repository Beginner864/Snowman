package com.example.musicapp.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id


@Entity
data class Song(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name = "song_id", columnDefinition = "serial")
    val id: Long? = null,  // 노래 고유 ID
    var title: String,     // 노래 제목
    var artist: String,    // 아티스트 이름
    var genre: String,     // 장르
    var mood: String,       // 기분
    var streamingUrl: String // url
)



