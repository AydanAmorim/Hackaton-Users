#
# Build stage
#
FROM maven:3.9.6-amazoncorretto-21 AS build

WORKDIR /authentication-api

COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B package -DskipTests

#
# Package stage
#
FROM amazoncorretto:21-alpine-jdk

WORKDIR /authentication-api

COPY --from=build /authentication-api/target/*.jar ./authentication-api.jar

EXPOSE 7072

ENTRYPOINT ["java","-jar","authentication-api.jar"]