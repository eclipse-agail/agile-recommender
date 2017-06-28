FROM agileiot/raspberry-pi3-zulujdk:8-jdk

# Add packages
RUN apt-get update && apt-get install --no-install-recommends -y \
    maven \
    && apt-get clean && rm -rf /var/lib/apt/lists/*

COPY ResourceOptimizer /usr/src/app
WORKDIR /usr/src/app
RUN ln -sf /usr/lib/jvm/ezdk*/bin/* /usr/bin/
RUN java -version
RUN mvn package
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar target/dockerservice-0.0.1-SNAPSHOT.jar" ]