# Etapa 1: Build do WAR com Maven
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copia o código e compila
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Deploy com Tomcat
FROM tomcat:10.1-jdk17

# Remove os apps padrão do Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copia o WAR gerado e renomeia para o contexto desejado
COPY --from=build /app/target/KronosCRUD-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/KronosCRUD_war_exploded.war

# Cria o arquivo de contexto para garantir o mapeamento correto
RUN mkdir -p /usr/local/tomcat/conf/Catalina/localhost && \
    echo '<?xml version="1.0" encoding="UTF-8"?>\n<Context path="/KronosCRUD_war_exploded" docBase="KronosCRUD_war_exploded" reloadable="true"/>' \
    > /usr/local/tomcat/conf/Catalina/localhost/KronosCRUD_war_exploded.xml

# Expõe a porta padrão do Tomcat
EXPOSE 8080

# Inicia o Tomcat
CMD ["catalina.sh", "run"]
