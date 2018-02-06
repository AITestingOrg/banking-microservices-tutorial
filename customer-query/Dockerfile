FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD build/libs/customer-query.jar app.jar
ENV JAVA_OPTS=""
EXPOSE 8081
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
