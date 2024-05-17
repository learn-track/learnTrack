FROM eclipse-temurin:21.0.2_13-jdk
EXPOSE $BACKEND_PORT
ADD build/libs/learnTrack.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]