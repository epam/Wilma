FROM debian

MAINTAINER Tamas Kohegyi <tkohegyi2@gmail.com>

RUN \
  mkdir /data && \
  mkdir /data/wilma && \
  cd /data/wilma && \
  apt-get update && \
  apt-get install -y apt-utils && \
  apt-get install -y unzip && \
  apt-get install -y wget && \
  apt-get install -y procps && \
  apt-get install -y openjdk-8-jre && \
  apt-get upgrade -y && \
  rm -rf /var/lib/apt/lists/* && \
  wget https://github.com/epam/Wilma/releases/download/V1.6.229/wilma-application-1.6.229.zip && \
  unzip wilma-application-1.6.229.zip && \
  mv wilma-1.6.229.jar wilma.jar && \
  rm -f wilma-application-1.6.229.zip

COPY start_wilma.sh /data/
RUN \
  cd /data && \
  chmod 777 *.sh

WORKDIR /data

ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64
ENV WILMA_CONFIGURATION wilma.conf.properties
#ENV WILMA_MX_SIZE - it has no default value
#ENV WILMA_KEYSTORE - it has no default value
#ENV WILMA_KEYSTORE_PASSWORD - it has no default value
#ENV WILMA_JMX_PORT 9011 - note, if you use it, the port should be exposed too!

# you may start wilma with default settings by /data/start_wilma.sh
CMD ["bash","./start_wilma.sh"]

# expose UI port and proxy port
EXPOSE 1234 9092
