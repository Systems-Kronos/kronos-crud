FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM tomcat:9.0-jdk17
WORKDIR /usr/local/tomcat
RUN rm -rf webapps/*
COPY --from=build /app/target/KronosCRUD-1.0-SNAPSHOT.war /tmp/KronosCRUD-1.0-SNAPSHOT.war
RUN mkdir -p webapps/KronosCRUD_war_exploded \
    && apt-get update && apt-get install -y unzip \
    && unzip /tmp/KronosCRUD-1.0-SNAPSHOT.war -d webapps/KronosCRUD_war_exploded
EXPOSE 8080
CMD ["catalina.sh", "run"]
