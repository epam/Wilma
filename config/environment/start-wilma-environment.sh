#!/bin/bash

# start wilma-ms
cd /opt/wilma/testenv/wilma-ms
nohup /usr/bin/java -Xmx512m -jar wilma-message-search.jar message.search.conf.properties > wilma-ms.out 2>&1 &

sleep 10s

# start wilma
cd /opt/wilma/testenv/wilma
nohup /usr/bin/java -Xmx512m -jar wilma.jar wilma.conf.properties > wilma.out 2>&1 &

# start test-server
cd /opt/wilma/testenv/test-server
nohup /usr/bin/java -Xmx256m -jar wilma-test-server.jar wilma.testserver.properties > test-server.out 2>&1 &

# start test-client - not needed for the automated tests