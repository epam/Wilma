#!/bin/bash

# clean up test environment
cd /opt/wilma/
rm -rf testenv
mkdir testenv
mkdir testenv/wilma
mkdir testenv/wilma-ms
mkdir testenv/test-server
mkdir testenv/test-client
chmod 777 testenv/*

# copy release to place
cp /var/lib/jenkins/jobs/wilma-continuous/workspace/wilma-application/build/distributions/*.zip testenv/wilma

cp /var/lib/jenkins/jobs/wilma-continuous/workspace/wilma-message-search/build/distributions/*.zip testenv/wilma-ms

cp /var/lib/jenkins/jobs/wilma-continuous/workspace/wilma-test/wilma-test-server/build/libs/*.jar testenv/test-server/wilma-test-server.jar
cp /var/lib/jenkins/jobs/wilma-continuous/workspace/wilma-test/wilma-test-server/wilma.testserver.properties testenv/test-server
cp -r /var/lib/jenkins/jobs/wilma-continuous/workspace/wilma-test/wilma-test-server/certificate testenv/test-server

cp /var/lib/jenkins/jobs/wilma-continuous/workspace/wilma-test/wilma-test-client/build/libs/*.jar testenv/test-client/wilma-test-client.jar
cp /var/lib/jenkins/jobs/wilma-continuous/workspace/wilma-test/wilma-test-client/wilma.testclient.properties testenv/test-client

# extract and prepare release
cd /opt/wilma/testenv/wilma
unzip *.zip
mv *.jar wilma.jar
mkdir log
chmod 777 log
mkdir messages
chmod 777 messages

cd /opt/wilma/testenv/wilma-ms
unzip *.zip
mv *.jar wilma-message-search.jar
mkdir log
chmod 777 log
