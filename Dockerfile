FROM  openjdk:14
COPY . .
ARG JAR_FILE=target/self-serve-api-0.0.1-SNAPSHOT-spring-boot.jar
COPY ${JAR_FILE} app.jar
CMD ["java","-jar","-Dspring.profiles.active=${environement}","/app.jar"]