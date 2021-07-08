This README is about an example test environment, right now used for continuous build and automated functional testing of Wilma

0. Environment preparation
- Latest stable Debian Linux
- extract OpenJdk 11 to /opt
- create folder /opt/wilma
- Jenkins installed

1. Jenkins setup
- wilma-continuous job exists, that is triggered when the code base changes
 wilma-continuous job executes this shell:

     chmod 755 ./gradlew

     #create "Wilma Stub Service and Proxy Application", "Wilma Message Search Application" and related applications/plugins
     #note: sonarqube target can be called as well with proper additional settings
     ./gradlew -PlocalRepository=file:///opt/wilma/repo/ clean docs build release -PbuildNumber=$BUILD_NUMBER

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

     #run functional test
     ./gradlew runFunctionalTest -PlocalRepository=file:///opt/wilma/repo/ -Pwilmahost=localhost -PbuildTag=$BUILD_NUMBER
