Wilma Plugin Example - Bulkhead
===============================

Purpose of this example
---------------------------------------
This plugin example was created to show an extra , maybe uncommon usage of the Wilma proxy. 

Implementation
---------------------------------------
TBD

Build
-----
```
#run extras: Bulkhead example
./gradlew -b wilma-extras/bulkhead/build.gradle clean build
```
After a successful run you will find the wilma plug-in in folder: wilma-extras/bulkhead/build/libs as a jar file.

Important Settings
---------------------------------------
Don't forget to **enable** the interceptors! Without that, the Bulkhead won't work properly (load to specific service cannot be measured).

REST Interface of Bulkhead plugin
---------------------------------
Note: In order to list the available services, use this call:
```
GET or POST http://localhost:1234/config/public/services/
```
See more information on using external service calls from Wilma plugins [here](https://github.com/epam/Wilma/wiki/Service-extensions-in-Plugins).
Also, in the examples below, it is assumed that the default configuration is in use and Wilma is used on `localhost`,
therefore the Wilma `<standardExternalServiceUrl>` is: `http://localhost:1234/config/public/services/`.

Get the actual load
-----------------------------------------
Get infomation on the load of the actual service.
```
GET http://localhost:1234/config/public/services/BulkheadInterceptor/loads 
```

TBD

