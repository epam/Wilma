Wilma Plugin Example - Bulkhead
===============================

Purpose of this example
---------------------------------------
This plugin example was created to show an extra, maybe uncommon usage of the Wilma proxy.
With this plugin, Wilma acts as a Bulkhead for a service. What "Bulkhead" means? - just search for "Bulkhead software pattern" on the web. 

Implementation
---------------------------------------
This is complex example, as not just acts as bulkhead for a service, but without modification, only by configuration, can act as bulkhead for several services with different settings.
In addition, it offers URL from where you may get information about the actual load.

To implement it, we used only 2 classes:
 - "BulkHeadChecker" acts as condition checker (determines if the load is high or not) and offers the logic of providing load information
of the services by using the "WilmaExternalService" interface.
 - "BulkHeadMapInformation" stores load information for a specific service
 
Please see `bulkHeadStubConfigExample.json` for example configuration. 
In the example configuration all the incoming requests are counted, and 5 hit/sec is the highest hit-rate that is allowed in bulkhead. 
In real life much higher numbers are used here. 

Build
-----
```
#run extras: Bulkhead example
./gradlew -b wilma-extras/bulkhead/build.gradle clean build
```
After a successful run you will find the wilma plug-in in folder: wilma-extras/bulkhead/build/libs as a jar file.

Important Settings
---------------------------------------
Don't forget to **enable** the interceptors! Without that, the Bulkhead won't work properly (the load of a specific service cannot be measured).

REST Interface of Bulkhead plugin
---------------------------------
Note: In order to list the available services, use this call:
```
GET or POST http://localhost:1234/config/public/services/
```
See more information on using external service calls from Wilma plugins [here](https://github.com/epam/Wilma/wiki/Service-extensions-in-Plugins).
Also, in the examples below, it is assumed that the default configuration is in use and Wilma is used on `localhost`,
therefore the Wilma `<standardExternalServiceUrl>` is: `http://localhost:1234/config/public/services/`.

Get the actual load from Bulkhead
---------------------------------
Get information on the load of the actual service.
```
GET http://localhost:1234/config/public/services/BulkHeadChecker/bulkhead
```
If it is empty, that means no load is measured yet.

How to Use it?
-----------------------------------------
- Ensure that the compiled bulkhead jar is available for your Wilma instance
- Configure Wilma to act as bulkhead (see example configuration above)
- Reach the service to be protected via Wilma as proxy, and Wilma will act as bulkhead for the service.

