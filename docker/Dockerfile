FROM maven:3.9.3-eclipse-temurin-17 as DEPS
WORKDIR /opt/app
COPY connector-aws-s3-libs/pom.xml connector-aws-s3-libs/pom.xml
COPY connector-aws-s3-standalone/pom.xml connector-aws-s3-standalone/pom.xml
COPY connector-aws-s3-example/pom.xml connector-aws-s3-example/pom.xml
COPY connector-file-api/pom.xml connector-file-api/pom.xml
COPY pom.xml .

RUN mvn -B -e -C org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline -DexcludeArtifactIds=connector-aws-s3-standalone,connector-aws-s3-libs,connector-file-api,connector-aws-s3-example

FROM maven:3.9.3-eclipse-temurin-17 as BUILDER
WORKDIR /opt/app
COPY --from=DEPS /root/.m2 /root/.m2
COPY --from=DEPS /opt/app/ /opt/app
COPY connector-aws-s3-libs/src /opt/app/connector-aws-s3-libs/src
COPY connector-aws-s3-standalone/src /opt/app/connector-aws-s3-standalone/src
COPY connector-aws-s3-example/src /opt/app/connector-aws-s3-example/src
COPY connector-file-api/src /opt/app/connector-file-api/src

RUN mvn -B -e clean install -DskipTests=true

FROM arm64v8/openjdk:17
WORKDIR /opt/app
COPY --from=BUILDER /opt/app/connector-aws-s3-standalone/target/connector-aws-s3-standalone-1.2.0-SNAPSHOT.jar /connector.jar
CMD [ "java", "-jar", "/connector.jar" ]
