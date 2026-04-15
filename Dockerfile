
# --- STAGE 1: Build the application ---
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the WAR file (skipping tests to speed up cloud deployment)
RUN mvn clean package -DskipTests

# --- STAGE 2: Run the application on GlassFish 8 ---
#FROM glassfish:8.0.0-jdk2: on Docker Hub
FROM ghcr.io/eclipse-ee4j/glassfish:8.0.0

# Set GlassFish environment variables
ENV GLASSFISH_HOME=/jakartaee/glassfish
ENV DEPLOY_DIR=${GLASSFISH_HOME}/glassfish/domains/domain1/autodeploy

# Remove default GlassFish apps to keep it clean
RUN rm -rf ${DEPLOY_DIR}/*

# Copy the WAR file from the build stage
# We rename it to ROOT.war so your site opens at the main URL (e.g., mysite.render.com/)
COPY --from=build /app/target/JSF_BabyEssential-1.0-SNAPSHOT.war ${DEPLOY_DIR}/ROOT.war

# Expose the standard HTTP port
EXPOSE 8080

# Start GlassFish in verbose mode so we can see logs in the cloud dashboard
CMD ["asadmin", "start-domain", "-v"]