FROM resin/raspberrypi3-openjdk:openjdk-8-jdk-20170217
VOLUME /tmp
ADD resourceoptimizer-0.0.1-SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
