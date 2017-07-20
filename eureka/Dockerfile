FROM java:8
VOLUME /tmp
ADD build/libs/eureka-server-0.1.0.jar app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 8761
ENTRYPOINT ["java","-jar","/app.jar"]