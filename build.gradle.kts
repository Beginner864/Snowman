plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.4.4"
	id("io.spring.dependency-management") version "1.1.7"
	kotlin("plugin.jpa") version "1.9.25"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	runtimeOnly("com.oracle.database.jdbc:ojdbc11")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	// jackon.module 추가
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("com.fasterxml.jackson.core:jackson-databind")
	implementation("com.fasterxml.jackson.core:jackson-annotations")

	// chat gpt 연동용 추가
	implementation("com.squareup.okhttp3:okhttp:4.12.0")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.3")
	implementation("io.github.cdimascio:dotenv-java:3.2.0") // dotenv-java 라이브러리 추가 .env 파일 위해서 (환경변수)

	// 로그인 추가
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("com.auth0:java-jwt:3.18.1")

	// 비밀번호 찾기 (메일 기능 추가)
	implementation ("org.springframework.boot:spring-boot-starter-mail")

}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
