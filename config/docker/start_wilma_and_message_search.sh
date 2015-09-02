#!/bin/bash

WILMA_BUILD=wilma-1.1.73.jar
WILMA_PID_FILE=/tmp/wilma.pid
WILMA_MX_SIZE=
WILMA_KEPYSTORE=
#WILMA_KESYTORE=-Djavax.net.ssl.keyStore=certificate/your.jks
WILMA_KEYSTORE_PASSWORD=
#WILMA_KEYSTORE_PASSWORD=-Djavax.net.ssl.keyStorePassword=password_for_your_jks
WILMA_START_CMD="nohup java -Dcom.sun.management.jmxremote.port=9011 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false $WILMA_MX_SIZE $WILMA_KEYSTORE $WILMA_KEYSTORE_PASSWORD -jar $WILMA_BUILD wilma.conf.properties & echo \$! > $WILMA_PID_FILE &"

WILMA_MS_BUILD=wilma-message-search-1.1.73.jar
WILMA_MS_PID_FILE=/tmp/wilma-search.pid
WILMA_MS_MX_SIZE=
WILMA_MS_START_CMD="nohup java -Dcom.sun.management.jmxremote.port=9010 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -jar $WILMA_MS_BUILD message.search.conf.properties & echo \$! > $WILMA_MS_PID_FILE &"
WILMA_MS_START_TIME=10

echo
echo "Welcome to dockerized Wilma & Wilma Message Search!"

echo
echo "[INFO] Docker image IP information:"
cat /etc/hosts

echo
echo "[INFO] Cleaning up lucene indexes..."
rm -f ./index/*
echo "[INFO] Starting Wilma Message Search Application..."
cd wilma-ms
eval ${WILMA_MS_START_CMD}
cd -
echo "[INFO] Wilma Message Search Application was started, process id is $(cat ${WILMA_MS_PID_FILE})"

echo "[INFO] Waiting $WILMA_MS_START_TIME seconds..."
sleep $WILMA_MS_START_TIME

echo "[INFO] Starting Wilma Application..."
cd wilma
eval ${WILMA_START_CMD}
cd -
echo "[INFO] Wilma Application was started, process id is $(cat ${WILMA_PID_FILE})"

