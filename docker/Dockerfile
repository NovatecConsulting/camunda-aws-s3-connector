FROM maven:3.9.3-eclipse-temurin-17
COPY ../pom.xml .
RUN mvn dependency:go-offline
COPY ../connector-aws-s3/src /src
RUN mvn clean package -DskipTests
COPY /target/connector-aws-s3-1.0.0-SNAPSHOT.jar /connector.jar
ENV AWS_ACCESS_KEY=xxx
ENV AWS_SECRET_KEY=xxx
# this is not yet working, since connection to Operate fails
ENTRYPOINT ["java","-jar","/connector.jar"]