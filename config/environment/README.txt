This README is about an example test environment, right now used for continuous build and automated functional testing of Wilma

0. Environment preparation
- Latest stable Debian Linux
- extract jdk1.8.0_181 to /opt ( so you will have /opt/jdk1.8.0_181 folder)
- create folder /opt/wilma
- Jenkins installed
- Sonar installed (standard installation, V7.x, so it is available on http://localhost:9000, the built in db is in use on port 39090)

1. Jenkins setup
- wilma-continuous job exists, that is triggered when the code base changes
 wilma-continuous job executes this shell:

     chmod 755 ./gradlew

     #create "Wilma Stub Service and Proxy Application" and "Wilma Message Search Application"
     ./gradlew -PlocalRepository=file:///opt/wilma/repo/ clean docs build sonarqube release -PbuildNumber=$BUILD_NUMBER

     #create test client
     ./gradlew -b wilma-test/wilma-test-client/build.gradle clean build -PbuildNumber=${BUILD_NUMBER} -x checkstyleMain -x checkstyleTest

     #create test server
     ./gradlew -b wilma-test/wilma-test-server/build.gradle clean build -PbuildNumber=${BUILD_NUMBER} -x checkstyleMain -x checkstyleTest

- wilma-deploy job exists that is triggered by every successful wilma-continuous job
 wilma-deploy job executes this shell:

     BUILD_ID=dontKillMe

     #copy scripts
     rm -f /opt/wilma/bin/*.sh
     cp /var/lib/jenkins/jobs/wilma-continuous/workspace/config/environment/*.sh /opt/wilma/bin
     chmod 755 /opt/wilma/bin/*.sh

     #execute scripts
     /opt/wilma/bin/stop-wilma-environment.sh
     sleep 3s
     /opt/wilma/bin/deploy-wilma-environment.sh
     /opt/wilma/bin/start-wilma-environment.sh

This will stop the previously deployed and started Wilma test environment, deploy the latest one, and start the environment again.
After this, the test environment is ready for integration/functional test.

2. Run Automated tests
- A Jenkins job exists (wilma-autotest) that is triggered by a successful wilma-deploy job.
 wilma-autotest job executes this shell

     chmod 755 ./gradlew

     #create a DEV version of the wilma-service-api first
     ./gradlew -b wilma-service-api/build.gradle clean build createPom uploadArchives -PlocalRepository=file:///opt/wilma/repo/

     #run functional test
     ./gradlew -b wilma-functionaltest/build.gradle clean run -PlocalRepository=file:///opt/wilma/repo/ -Pwilmahost=localhost -PbuildTag=$BUILD_NUMBER
