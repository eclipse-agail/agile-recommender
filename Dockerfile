FROM resin/raspberrypi3-openjdk:openjdk-8-jdk-20170217
RUN mvn package
RUN sh -c 'touch /dockerservice 0.0.1-SNAPSHOT.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /dockerservice 0.0.1-SNAPSHOT.jar" ]