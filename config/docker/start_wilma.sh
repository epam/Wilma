#!/bin/bash

WILMA_BUILD=wilma.jar
#WILMA_CONFIGURATION is an external environment variable, usually: wilma.conf.properties
#WILMA_MX_SIZE is an external environment variable, for example: -Xmx8G
#WILMA_KEPYSTORE is an external environment variable, for example: -Djavax.net.ssl.keyStore=certificate/your.jks
#WILMA_KEYSTORE_PASSWORD is an external environment variable, example: -Djavax.net.ssl.keyStorePassword=password_for_your_jks
#WILMA_JMX_PORT - Wilma JMX port, if empty, no JMX used, otherwise the JMX port is fully opened

if [ -n "$WILMA_JMX_PORT" ]; then
    WILMA_JMX="-Dcom.sun.management.jmxremote.port=$WILMA_JMX_PORT -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"
fi
WILMA_START_CMD="java $WILMA_JMX $WILMA_MX_SIZE $WILMA_KEYSTORE $WILMA_KEYSTORE_PASSWORD -jar $WILMA_BUILD $WILMA_CONFIGURATION"

echo
echo "Welcome to dockerized Wilma!"

echo
echo "[INFO] Docker image IP information:"
cat /etc/hosts

echo
echo "[INFO] Starting Wilma Application, using command: $WILMA_START_CMD"
echo
cd wilma
eval ${WILMA_START_CMD}
