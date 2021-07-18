# Pulling base Image
FROM openjdk:8-jre-alpine

# Listening port
EXPOSE 8080

# Working Directory defined
WORKDIR /app

# Jar file should be at the root
COPY target/marketplace-core-service-0.0.1-SNAPSHOT.jar .

# Kafka security key
#COPY src/main/resources/keystore.jks .

# MongoDB security key
COPY certstore-db.jks .
COPY keystore-db.jks .

# Application initiating
ENTRYPOINT [ "java", "-jar", "marketplace-core-service-0.0.1-SNAPSHOT.jar" ]