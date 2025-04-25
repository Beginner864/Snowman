package com.example.musicapp

import io.github.cdimascio.dotenv.Dotenv
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MusicappApplication

fun main(args: Array<String>) {

	// Render 환경이 아니라면 .env를 불러와 시스템 변수에 등록
	if (System.getenv("RENDER") == null) {
		val dotenv = Dotenv.configure().load()
		dotenv.entries().forEach { entry ->
			System.setProperty(entry.key, entry.value)
		}
	}

	// 실행부
	runApplication<MusicappApplication>(*args)
}
