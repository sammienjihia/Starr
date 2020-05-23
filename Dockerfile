# Base image containing the Java runtime
# A multistage build where we seperate the build stage from the runtime stage
# Stage one base image for compilation and packaging
FROM openjdk:11-jdk-slim AS build

# Maintainer information
LABEL maintainer="sammienjihia@gmail.com"

ENV APP_HOME=/usr/app/

# Define the working directory in the docker container
WORKDIR $APP_HOME

# Copy local project to container's working directory
COPY . .

# Use gradle wrapper to clean(remove the previous builds) and build the project. No need for gradle installation
# with build assemble, lint and test shall be executed
RUN ./gradlew clean assemble

# Stage two: runtime
FROM openjdk:11.0-jre-slim

LABEL maintainer="sammienjihia@gmail.com"

ENV APP_HOME=/usr/app/

WORKDIR $APP_HOME

COPY --from=build $APP_HOME/build/libs/starr-0.0.1-SNAPSHOT.jar .

EXPOSE 8080
#CMD["sh", "java","-jar","$APP_HOME/starr-0.0.1-SNAPSHOT.jar"]
#CMD "java -jar starr-0.0.1-SNAPSHOT.jar"
ENTRYPOINT ["java", "-jar", "starr-0.0.1-SNAPSHOT.jar"]
