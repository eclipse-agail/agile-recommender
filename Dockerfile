ARG BASEIMAGE_BUILD=agileiot/raspberry-pi3-zulujdk:8-jdk-maven
ARG BASEIMAGE_DEPLOY=agileiot/raspberry-pi3-zulujdk:8-jre

FROM $BASEIMAGE_BUILD
COPY RecommenderService /usr/src/app
WORKDIR /usr/src/app
RUN ln -sf /usr/lib/jvm/ezdk*/bin/* /usr/bin/
RUN java -version
RUN mvn package
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar target/recommenderservice-0.0.1-SNAPSHOT.jar" ]

FROM $BASEIMAGE_DEPLOY
COPY --from=0 /usr/src/app usr/src/app
WORKDIR /usr/src/app
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar target/recommenderservice-0.0.1-SNAPSHOT.jar" ]
