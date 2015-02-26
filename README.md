Wilma
===========
Wilma consists of two applications:

* Wilma application
* Wilma Message Search

**Wilma application** acts as an intermediary between a client and a server (communicating over HTTP/HTTPS), it logs the messages, and makes it possible to stub/mock requests based on the configuration defined in an XML file. Also configurable via plugins.
**Message Search** provides high performance searching of the request response pairs that were logged by Wilma. 

#For users
##Wilma application
###Requirements
* JRE 7
* The latest release of Wilma application.

###Running
`java -jar wilma-x.y.z.jar wilma.conf.properties`

##Wilma Message Search
###Requirements
* JDK 7
* The latest release of Wilma application.

###Running
`java -jar wilma-message-search-x.y.z.jar wilma.conf.properties`

#For developers

##Requirements
* Java JDK 7

##Advised working environment
* Eclipse
* Gradle Integration for Eclipse,  Eclipse plugin - To support gradle projects in Eclipse. Install it through Eclipse Marketplace 
* Checkstyle Eclipse plugin - use the following update site to install: http://eclipse-cs.sf.net/update/

##Building with Gradle
The project can be built with by executing the following command from the root directory which contains the project (the folder which contains the gradlew file):

`gradlew clean build`

###If you only want to build Wilma application
`gradlew clean :wilma-application:wilma-engine:build`
###If you only want to build Wilma Message Search
`gradlew clean :wilma-message-search:wilma-message-search-engine:build`

##Running with Gradle
The project can be run by executing the following command from the root directory which contains the project (the folder which contains the gradlew file).

###Wilma application
`gradlew -q :wilma-application:wilma-engine:run`

###Wilma Message Search
`gradlew -q :wilma-message-search:wilma-message-search-engine:run`

#For further information
* Check the Wiki
* etc.

# License
Copyright 2013-2015 EPAM Systems

Wilma is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Wilma is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Wilma.  If not, see <http://www.gnu.org/licenses/>.