Wilma
===========
Wilma is a Java based proxy and stub solution. It consists of two applications:

* Wilma application
* Wilma Message Search application

**Wilma application** acts as a proxy between two components those are communicating over HTTP/HTTPS. It logs the messages, and makes it possible to stub/mock requests based on the configuration defined in an XML file(s). Also configurable via plugins.
**Wilma Message Search application** provides high performance searching of the request-response pairs that were logged by Wilma application.

#Quick intro for end users
##Wilma application
####Requirements
* JRE 7
* The latest release of Wilma application.

####Running
`java -jar wilma-x.y.z.jar wilma.conf.properties`

##Wilma Message Search application
####Requirements
* JDK 7
* The latest release of Wilma Message Search application.

####Running
`java -jar wilma-message-search-x.y.z.jar wilma.message.conf.properties`

#Quick intro for developers/contributors

####Requirements
* Java JDK 7

####Advised working environment
* Eclipse / IntelliJ
* Gradle, Checkstyle, Git Integration for the IDE

####Building with Gradle
The project can be built with by executing the following command from the root directory which contains the project (the folder which contains the gradlew file):

`gradlew clean build`

####Running with Gradle
The project can be run by executing the following command from the root directory which contains the project (the folder which contains the gradlew file).

#####Wilma application
`gradlew -q :wilma-application:wilma-engine:run`

#####Wilma Message Search application
`gradlew -q :wilma-message-search:wilma-message-search-engine:run`

##For further information
* Check the Wiki and Issues in GitHub
* Check further documentation at http://epam.github.io/Wilma/

## Contact
Follow Wilma on Twitter ([@epam-wilma](http://twitter.com/epam-wilma))

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

##Contribution

There are three ways you can help us:

* **Raise an issue.** You found something that does not work as expected? Let us know about it.
* **Suggest a feature.** It's even better if you come up with a new feature and write us about it.
* **Write some code.** We would love to see more pull requests to our framework, just make sure you have the latest sources.