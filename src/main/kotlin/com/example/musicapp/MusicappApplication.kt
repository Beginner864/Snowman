package com.example.musicapp

import io.github.cdimascio.dotenv.Dotenv
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MusicappApplication

fun main(args: Array<String>) {

	// .env 파일이 spring에서 인식되지 않는 것을 해결하기 위함
	val dotenv = Dotenv.configure().load()
	dotenv.entries().forEach { entry ->
		System.setProperty(entry.key, entry.value)
	}

	// 실행부
	runApplication<MusicappApplication>(*args)
}
