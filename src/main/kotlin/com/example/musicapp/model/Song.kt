package com.example.musicapp.model

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*

@Entity
data class Song(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,  // 노래 고유 ID
    var title: String,     // 노래 제목
    var artist: String,    // 아티스트 이름
    var genre: String,     // 장르
    var mood: String,      // 기분

    @Column(name = "streaming_url")
    @JsonProperty("streaming_url")
    var streamingUrl: String, // URL

    // USER 테이블과의 관계 (외래 키)
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference  // 순환 참조 방지: Song -> User 방향에서만 직렬화됨
    var user: User // user는 필수
)





