FROM resin/raspberry-pi3-openjdk:openjdk-8-jdk-20170426
# Add packages
RUN apt-get update && apt-get install --no-install-recommends -y \
    maven \
    && apt-get clean && rm -rf /var/lib/apt/lists/*
COPY ResourceOptimizer /usr/src/app
WORKDIR /usr/src/app
RUN mvn package
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar target/dockerservice-0.0.1-SNAPSHOT.jar" ]