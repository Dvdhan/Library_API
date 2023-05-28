FROM openjdk:11-jdk as build
WORKDIR /workspace/app

COPY . /workspace/app

RUN ./gradlew build -x test

# 실행 단계
FROM openjdk:11-jre-slim

# 빌드 단계에서 생성된 jar 파일을 현재 위치(/app)로 복사
COPY --from=build /workspace/app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]


