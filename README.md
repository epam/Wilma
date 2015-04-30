Wilma
===========
Wilma is a Java based proxy and stub application, or in other words, a Service Virtualization solution. It consists of two applications:

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
`java -jar wilma-message-search-x.y.z.jar message.search.conf.properties`

#Quick intro for developers/contributors

####Requirements
* Java JDK 7

####Advised working environment
* Eclipse / IntelliJ
* Gradle, Checkstyle, Git Integration for the IDE

####Building with Gradle
The project can be built by following the instructions described [here](https://github.com/epam/Wilma/wiki/DEV,-Build-from-Scratch).
This way of build is recommended for contributors only, End-Users, please use the pre-built downloadable releases from [here](https://github.com/epam/Wilma/releases).

####Running with Gradle
Please see detailed information on how to run **Wilma** and **Wilma Message Search** applications [here](http://epam.github.io/Wilma/endusers/index.html).

##Detailed information
* Check the Wiki and Issues link on GitHub
* Check further documentation at http://epam.github.io/Wilma/

## Contact, questions and answers
Post your request to Wilma-Users [mailing list](https://groups.google.com/forum/#!forum/wilma-users), or send direct mail to tkohegyi2<at>gmail.com.

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
* **Write some code.** We would love to see pull requests to this tool, just make sure you have the latest sources.