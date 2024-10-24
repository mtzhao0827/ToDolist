FROM maven:3-amazoncorretto-17-alpine as build

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline

COPY . .
RUN mvn clean package

FROM eclipse-temurin:17-jre-noble

COPY --from=build /app/target/*.jar ToDolist.jar

ENTRYPOINT ["java", "-jar","/ToDolist.jar"]