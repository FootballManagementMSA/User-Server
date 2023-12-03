FROM openjdk:17
ARG JAR_FILE=build/libs/*.jar
ARG DEBIAN_FRONTEND=noninteractive
ENV TZ=Asia/Seoul
RUN apt-get update && apt-get install -y tzdata
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
