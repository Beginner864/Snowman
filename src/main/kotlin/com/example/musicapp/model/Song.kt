package com.example.musicapp.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*


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

    @Column(name = "streaming_url")
    @JsonProperty("streaming_url")
    var streamingUrl: String, // url

    // USER 테이블과의 관계 (외래 키)
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    var user: User // user는 필수
)



