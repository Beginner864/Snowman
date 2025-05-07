package com.example.musicapp.model

import jakarta.persistence.*

@Entity
@Table(name = "users")  // 테이블 이름을 users로 변경
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")  // USER_ID 컬럼을 명시적으로 지정
    val id: Long? = null,
    val username: String,
    var password: String,

    // USER가 여러 개의 SONG을 가질 수 있음 (1:N 관계)
    @OneToMany(mappedBy = "user")
    val songs: List<Song> = emptyList() // 여러 개의 Song을 가지고 있을 수 있음
)

