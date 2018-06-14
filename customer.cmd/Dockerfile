FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD build/libs/customer-cmd-0.0.1.jar app.jar
ENV JAVA_OPTS=""
EXPOSE 8080
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
