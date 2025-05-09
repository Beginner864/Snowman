package com.example.musicapp.model

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*

@Entity
@Table(name = "users")  // 테이블 이름을 users로 변경
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val username: String,
    var password: String,
    var email: String,  // 이메일 추가

    // USER가 여러 개의 SONG을 가질 수 있음 (1:N 관계)
    @OneToMany(mappedBy = "user", cascade = [CascadeType.REMOVE])
    @JsonManagedReference  // 순환 참조 방지: User -> Song 방향에서만 직렬화됨
    val songs: List<Song> = emptyList() // 여러 개의 Song을 가지고 있을 수 있음
)


