# 1단계: Gradle로 JAR 빌드
FROM gradle:8.4.0-jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle build --no-daemon

# 2단계: 빌드된 JAR로 실제 앱 실행
# JDK 17 기반 슬림 이미지 사용
FROM openjdk:17-jdk-slim

# 작업 디렉토리 설정
WORKDIR /app

# 빌드된 jar 파일 복사 (이름은 실제 jar 파일명으로)
COPY --from=builder /app/build/libs/musicapp-0.0.1-SNAPSHOT.jar app.jar


# 실행 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]