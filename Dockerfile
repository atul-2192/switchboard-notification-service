# ---------- Build stage ----------
FROM maven:3.9.9-eclipse-temurin-17 AS build

# Build arguments for GitHub Packages authentication
ARG GITHUB_ACTOR
ARG GITHUB_TOKEN

WORKDIR /build
COPY pom.xml .
COPY src ./src

# Create Maven settings.xml with GitHub authentication
RUN mkdir -p /root/.m2 && \
    echo '<settings><servers><server>' > /root/.m2/settings.xml && \
    echo "<id>github</id><username>${GITHUB_ACTOR}</username>" >> /root/.m2/settings.xml && \
    echo "<password>${GITHUB_TOKEN}</password>" >> /root/.m2/settings.xml && \
    echo '</server></servers></settings>' >> /root/.m2/settings.xml && \
    mvn clean package -DskipTests

# ---------- Runtime stage ----------
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /build/target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
