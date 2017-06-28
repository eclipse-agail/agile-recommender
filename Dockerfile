FROM agileiot/raspberry-pi3-zulujdk:8-jdk-maven

COPY ResourceOptimizer /usr/src/app
WORKDIR /usr/src/app
RUN ln -sf /usr/lib/jvm/ezdk*/bin/* /usr/bin/
RUN java -version
RUN mvn package
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar target/dockerservice-0.0.1-SNAPSHOT.jar" ]

FROM agileiot/raspberry-pi3-zulujdk:8-jre
#FROM resin/raspberry-pi3-openjdk:openjdk-8-jdk-20170426
COPY --from=0 /usr/src/app usr/src/app
WORKDIR /usr/src/app
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar target/dockerservice-0.0.1-SNAPSHOT.jar" ]
