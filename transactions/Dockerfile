FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD build/libs/transactions-0.0.1-SNAPSHOT.jar app.jar
ENV JAVA_OPTS=""
EXPOSE 8085
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
