FROM openjdk:17-jdk-slim AS runtime

# curl 설치 (헬스체크용) openjdk:17 (slim버전이 아닌 full 버전)로 설치하고 RUN줄 삭제 해도 됨
RUN apt-get update \
 && apt-get install -y --no-install-recommends curl \
 && rm -rf /var/lib/apt/lists/*

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} sumte.jar

HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=5 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "sumte.jar"]