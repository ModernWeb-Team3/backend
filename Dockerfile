FROM eclipse-temurin:17-alpine

# Setting Timezone to UTC
RUN apk add tzdata
RUN ln -s /usr/share/zoneinfo/Asia/Seoul /etc/localtime
RUN echo "Etc/UTC" > /etc/timezone

# Install curl for HealthCheck
RUN apk --no-cache add curl

# Extract Server Jar
ARG JAR_FILE=build/libs/backend*.jar
COPY ${JAR_FILE} unideal-backend.jar

# healthcheck - TODO: implement when healthcheck is implemented
# HEALTHCHECK --interval=10s --timeout=3s --retries=3 CMD curl -f http://127.0.0.1/hello || exit 1

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-server", "-jar", "./unideal-backend.jar"]