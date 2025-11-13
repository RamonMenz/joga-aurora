# -------------------------------
# Etapa 1: build da aplicação
# -------------------------------
FROM maven:3.9.9-eclipse-temurin-21 AS builder

# Define o diretório de trabalho
WORKDIR /app

# Copia o arquivo pom.xml e baixa dependências
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o restante do projeto e compila
COPY . .
RUN mvn clean package -DskipTests

# -------------------------------
# Etapa 2: imagem final (leve)
# -------------------------------
FROM eclipse-temurin:21-jre

# Diretório de trabalho dentro do container
WORKDIR /app

# Copia o JAR gerado da etapa anterior
COPY --from=builder /app/target/*.jar app.jar

# Define a porta padrão do Spring Boot
EXPOSE 8080

# Comando de inicialização
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
