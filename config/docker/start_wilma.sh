#!/bin/bash

WILMA_BUILD=wilma-1.1.73.jar
WILMA_PID_FILE=/tmp/wilma.pid
WILMA_MX_SIZE=
WILMA_KEPYSTORE=
#WILMA_KESYTORE=-Djavax.net.ssl.keyStore=certificate/your.jks
WILMA_KEYSTORE_PASSWORD=
#WILMA_KEYSTORE_PASSWORD=-Djavax.net.ssl.keyStorePassword=password_for_your_jks
WILMA_START_CMD="nohup java -Dcom.sun.management.jmxremote.port=9011 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false $WILMA_MX_SIZE $WILMA_KEYSTORE $WILMA_KEYSTORE_PASSWORD -jar $WILMA_BUILD wilma.conf.properties & echo \$! > $WILMA_PID_FILE &"

echo
echo "Welcome to dockerized Wilma!"

echo
echo "[INFO] Docker image IP information:"
cat /etc/hosts

echo "[INFO] Starting Wilma Application..."
eval ${WILMA_START_CMD}
echo "[INFO] Wilma Application was started, process id is $(cat ${WILMA_PID_FILE})"

