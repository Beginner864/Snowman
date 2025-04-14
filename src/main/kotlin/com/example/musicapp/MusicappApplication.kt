package com.example.musicapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MusicappApplication

fun main(args: Array<String>) {
	runApplication<MusicappApplication>(*args)
}
