
FROM maven:3.8.5-eclipse-temurin-17 AS build

WORKDIR /myAppBooks

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-alpine AS runtime

RUN addgroup -S spring && adduser -S spring -G spring

WORKDIR /myAppBooks

COPY --from=build /myAppBooks/target/hw16-docker-3.5.3.jar myAppBooks.jar

RUN chown spring:spring myAppBooks.jar

USER spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "myAppBooks.jar"]