# this starts from Wilma base, so just adds the message search related things
FROM epam/wilma

MAINTAINER Tamas Kohegyi <tkohegyi2@gmail.com>

RUN \
  mkdir /data/wilma-ms && \
  mkdir /data/wilma/messages && \
  cd /data/wilma-ms && \
  apt-get update && \
  apt-get upgrade -y && \
  apt-get install -y openjdk-8-jdk && \
  rm -rf /var/libs/apt/lists/* && \
  wget https://github.com/epam/Wilma/releases/download/V1.6.229/wilma-message-search-1.6.229.zip && \
  unzip -o wilma-message-search-1.6.229.zip && \
  mv wilma-message-search-1.6.229.jar wilma-message-search.jar && \
  rm -f wilma-message-search-1.6.229.zip

COPY start_wilma_and_message_search.sh /data/
RUN \
  cd /data && \
  chmod 777 *.sh

WORKDIR /data

ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64
#ENV WILMA_MS_MX_SIZE - it has no default value
ENV WILMA_MS_CONFIGURATION message.search.conf.properties
#ENV WILMA_MS_JMX_PORT 9010 - note, if you use it, the port should be exposed too

# you may start wilma and message search with default setting by /data/start_wilma_and_message_search.sh
CMD ["bash","./start_wilma_and_message_search.sh"]

# expose UI port of message search application
EXPOSE 9093