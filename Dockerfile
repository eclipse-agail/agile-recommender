FROM resin/raspberrypi3-openjdk:openjdk-8-jdk-20170217
VOLUME /tmp
RUN sh -c 'javac *.java'
RUN sh -c 'jar cvfe app.jar RnCAPI *.class'
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]