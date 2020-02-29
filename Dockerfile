FROM openjdk:8-jdk-alpine
# Set app home folder
ENV APP_HOME /app
# Set jar file name
ENV JAR_FILE appband-api.jar

RUN mkdir $APP_HOME
WORKDIR $APP_HOME
ADD target/*.jar $APP_HOME/$JAR_FILE
VOLUME $APP_HOME/logs

# Set default value for variables on entrypoint
ENV SPRING_PROFILE="homolog"
ENV JAVA_OPTS=""

ENTRYPOINT java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom \
	-Dspring.profiles.active="$SPRING_PROFILE" \
    -Dspring.data.mongodb.uri="$SPRING_DATA_MONGODB_URI" \
	-jar $JAR_FILE

EXPOSE 8080