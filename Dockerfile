FROM maven:3.9.3-eclipse-temurin-17
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src /src
RUN mvn clean package -DskipTests
COPY /target/connector-aws-s3-1.0.0-SNAPSHOT.jar /connector.jar
ENTRYPOINT ["java","-jar","/connector.jar"]