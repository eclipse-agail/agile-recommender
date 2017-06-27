FROM resin/raspberry-pi3-debian

#
# Pull Zulu OpenJDK binaries from official repository:
#
#RUN apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys 0x219BD9C9
#RUN echo "deb http://repos.azulsystems.com/ubuntu stable main" >> /etc/apt/sources.list.d/zulu.list
#RUN apt-get update
#RUN apt-get -y install zulu-8=8.21.0.1

RUN apt-get update && apt-get install --no-install-recommends -y \
    curl \
    && apt-get clean && rm -rf /var/lib/apt/lists/*
RUN curl -O http://cdn.azul.com/zulu-embedded/bin/ezdk-1.8.0_121-8.20.0.42-eval-linux_aarch32hf.tar.gz
RUN mkdir -p /usr/lib/jvm && \
    tar xf ezdk*.tar.gz -C /usr/lib/jvm
RUN ln -s /usr/lib/jvm/ezdk*/bin/java /usr/bin/
RUN java -version

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