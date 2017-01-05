Wilma
===========
Wilma is a **Service Virtualization** tool, that combines the capabilities of a **Service Stub** and a **HTTP/HTTPS Transparent Proxy**. 
Its main purpose is to support development and testing applications/services/components those functionality relies/depends on another - usually 3rd party - service or services. 
The selected architecture approach makes it capable to use it in performance test environment and environment that contains microservices. 
It is written in Java, and the solution consists of two standalone applications:

* **Wilma** application
* **Wilma Message Search** application

**Wilma** application is the highly configurable **Service Virtualization** tool. 
It logs the messages, and meanwhile acts as a proxy between the components, makes it possible to stub responses based on configuration defined in XML file(s).
It is designed for use in test environments where test automation is in use (unit, integration, functional or end-to-end).
Also can be used for manual tests. It is expandable easily via plugins.

**Wilma Message Search** application provides high performance searching of the request-response pairs that were logged by Wilma application.

#Quick intro for end users
##Wilma application
####Requirements
* JRE 7 or 8
* The latest [release](https://github.com/epam/Wilma/releases) of Wilma application downloaded and extracted into a folder.

####Running
`java -jar wilma-x.y.z.jar wilma.conf.properties`

####Configuring Components/Services to use Wilma
The most simple way to do this is by configuring the Component/Service to use Wilma as HTTP(S) proxy. 
In case of Java components/services, this can be done by adding a few VM arguments to the run configuration:

```
-DproxyHost=[wilma-url]
-DproxyPort=[wilma-port]
```

See [this page](http://epam.github.io/Wilma/endusers/index.html) for more detailed information on how to configure Wilma, and Component/Service that uses Wilma.

####Notes
* **Docker image** of Wilma is available on DockerHub, see details [here](https://github.com/epam/Wilma/wiki/Docker-image-of-Wilma)

##Wilma Message Search application
####Requirements
* JDK 7 or 8
* The latest [release](https://github.com/epam/Wilma/releases) of Wilma Message Search application downloaded and extracted into a folder.

####Running
`java -jar wilma-message-search-x.y.z.jar message.search.conf.properties`

####Notes
* Running Wilma Message Search application is optional, Wilma itself does not require it.
* To run Wilma Message Search application, java JDK must be used. With JRE, it will not work properly.
* **Docker image** of combined Wilma and Wilma Message Search application is available on DockerHub, see details [here](https://github.com/epam/Wilma/wiki/Docker-image-of-Wilma)

#Quick intro for developers/contributors

####Requirements
* Java JDK 8

####Advised working environment
* IntelliJ / Eclipse
* Gradle, Checkstyle, Git Integration for the IDE

####Building with Gradle
The project can be built by following the instructions described [here](https://github.com/epam/Wilma/wiki/DEV,-Build-from-Scratch).
This way of build is recommended for contributors only, End-Users, please use the pre-built downloadable releases from [here](https://github.com/epam/Wilma/releases).
Actual build status: [![Build Status](https://travis-ci.org/epam/Wilma.svg?branch=master)](https://travis-ci.org/epam/Wilma)

##Detailed information
* Check the [Wiki](https://github.com/epam/Wilma/wiki) and [Issues](https://github.com/epam/Wilma/issues) link on GitHub
* Check further documentation at [http://epam.github.io/Wilma/](http://epam.github.io/Wilma/)

## Contact, questions and answers
In order to get the latest news, follow this project on GitHub.

Feel free to contribute (send pull request), or seek for assistance/advise, or discuss usage scenarios by joining to [wilma-users](https://groups.google.com/forum/#!forum/wilma-users) mailing list.

# License - GPLv3.0
Copyright 2013-2017 EPAM Systems

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

* **Raise an [issue](https://github.com/epam/Wilma/issues).** You found something that does not work as expected? Let us know about it.
* **Suggest a [feature](https://groups.google.com/forum/#!forum/wilma-users).** It's even better if you come up with a new feature and write us about it.
* **Write some code.** We would love to see pull requests to this tool, just make sure you have the latest sources.