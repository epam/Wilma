#!/bin/bash

WILMA_BUILD=wilma.jar
#WILMA_CONFIGURATION is an external environment variable, usually: wilma.conf.properties
#WILMA_MX_SIZE is an external environment variable, for example -Xmx8G
#WILMA_KESYTORE is an external environment variable, for example: -Djavax.net.ssl.keyStore=certificate/your.jks
#WILMA_KEYSTORE_PASSWORD is an external environment variable, for example: -Djavax.net.ssl.keyStorePassword=password_for_your_jks
#WILMA_JMX_PORT - Wilma JMX port, if empty, no JMX used, otherwise the JMX port is fully opened

if [ -n "$WILMA_JMX_PORT" ]; then
    WILMA_JMX="-Dcom.sun.management.jmxremote.port=$WILMA_JMX_PORT -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"
fi
WILMA_START_CMD="java $WILMA_JMX $WILMA_MX_SIZE $WILMA_KEYSTORE $WILMA_KEYSTORE_PASSWORD -jar $WILMA_BUILD $WILMA_CONFIGURATION"

WILMA_MS_BUILD=wilma-message-search.jar
#WILMA_MS_CONFIGURATION is an external environment variable, usually: message.search.conf.properties
#WILMA_MS_MX_SIZE is an external environment variable, for example: -Xmx8G
#WILMA_MS_JMX_PORT - Wilma Message Search JMX port, if empty, no JMX used, otherwise the JMX port is fully opened
WILMA_MS_START_CMD="java $WILMA_MS_JMX $WILMA_MS_MX_SIZE -jar $WILMA_MS_BUILD $WILMA_MS_CONFIGURATION &"
WILMA_MS_START_TIME=10

echo
echo "Welcome to dockerized Wilma & Wilma Message Search!"

echo
echo "[INFO] Docker image IP information:"
cat /etc/hosts

echo
echo "[INFO] Cleaning up lucene indexes..."
rm -f ./index/*

echo
echo "[INFO] Starting Wilma Message Search Application..."
cd wilma-ms
eval ${WILMA_MS_START_CMD}
cd -

echo
echo "[INFO] Waiting $WILMA_MS_START_TIME seconds..."
sleep $WILMA_MS_START_TIME

echo "[INFO] Starting Wilma Application..."
cd wilma
eval ${WILMA_START_CMD}
