Wilma
===========
Wilma is a **Service Virtualization** tool, that combines the capabilities of a **Service Stub** and a **HTTP/HTTPS Transparent Proxy**. 
Its main purpose is to support development and testing applications/services/components those functionality relies/depends on another application/services/components that can be owned by you or can be owned by 3rd party as well. 
The selected architecture approach makes it capable to use it in unit, integration, functional, end-to-end, performance test environments and environments those contains microservices. 
It can be used for manual tests too. It is expandable easily via plugins and configurable on-the-fly.
It is written in Java, and the solution consists of two standalone applications:

* **Wilma** application is the highly configurable **Service Virtualization** tool
* **Wilma Message Search** application (optional component) provides high performance searching capability of the request-response pairs that were logged by Wilma application.

# Quick intro for end users
## Wilma application
#### Requirements
* JRE 15 for V2.3.x releases of Wilma, and above
* JRE 11 for V2.0.x-V2.2.x releases of Wilma
* JRE 8 for V1.6.x releases of Wilma
* JRE 7 in case version earlier than V1.6.x in use
* The latest [release](https://github.com/epam/Wilma/releases) of Wilma application downloaded and extracted into a folder.
#### Note on V1.6 and V2.0 versions
Please be noted that there were non-backward compatible changes when Wilma is upgraded from V1.6 to V2.0. Configurations used in V1.6 (that was xml based) cannot be used for configuring V2.0 (which is json based). You should also know that V1.6 release works reliable with HTTP only, because the supported HTTPS protocols (SSLv3, TLS1) become obsolete in the meantime. On the other hand, V2.0 can be used for TLS1.1, TLS 1.2 and even for TLS1.3  - however extra configurations may be necessary and unexpected events may occur (like the used jdk does not support some of these protocols fully). HTTP part shall work without issue.

### Configuring Components/Services to use Wilma
The most simple way to do this is by configuring the Component/Service to use Wilma **as HTTP(S) proxy**. 
In case of Java components/services, this can be done by adding a few VM arguments to the run configuration:

```
JAVA_PROXY_FLAGS=-Dhttp.proxyHost=[wilma-url] -Dhttp.proxyPort=[wilma-proxy-port] -Dhttps.proxyHost=[wilma-url] -Dhttps.proxyPort=[wilma-proxy-port]
java ${JAVA_PROXY_FLAGS} ...
```

### Configure and run Wilma

To run Wilma with simplest configuration, just download the release, extract it and run:
`java -jar wilma-x.y.z.jar wilma.conf.properties`

See [this page](http://epam.github.io/Wilma/endusers/index.html) for more detailed information on how to configure Wilma, and Component/Service that uses Wilma.

In case you would like to use Wilma with its HTTPS support, it is suggested to start Wilma with additional parameters:

```java -Djdk.tls.namedGroups="secp256r1, secp384r1, ffdhe2048, ffdhe3072" -Djdk.tls.client.protocols="TLSv1,TLSv1.1,TLSv1.2" -jar wilma-x.y.z.jar wilma.conf.properties```

The necessary additional parameters will always depend on the actual environment, for example specifying the protocols might not be necessary in many cases.


#### Docker Image of Wilma
* **Docker image** of Wilma is available on DockerHub, see details [here](https://github.com/epam/Wilma/wiki/Docker-image-of-Wilma)

## Contact, questions and answers
In order to get the latest news, follow this project on GitHub.
The latest documentation can be found at [http://epam.github.io/Wilma/](http://epam.github.io/Wilma/).
Feel free to seek for assistance/advise, or discuss usage scenarios by submitting new [Issue](https://github.com/epam/Wilma/issues) on GitHub.

## Wilma Message Search application
* Running Wilma Message Search application is optional, Wilma itself does not require it.
* This optional component just makes your life easier in case you would like to run quick searches on the messages logged by Wilma.
* To run Wilma Message Search application, java JDK must be used. With JRE, it will not work properly.
* **Docker image** of combined Wilma and Wilma Message Search application is available on DockerHub, see details [here](https://github.com/epam/Wilma/wiki/Docker-image-of-Wilma)

#### Requirements
* JDK 15 (pls use earlier Wilma [releases](https://github.com/epam/Wilma/releases) than V2.0.x when you need to use Java 8 version)
* The latest [release](https://github.com/epam/Wilma/releases) of Wilma Message Search application downloaded and extracted into a folder.

#### Running
`java -jar wilma-message-search-x.y.z.jar message.search.conf.properties`

# Quick intro for developers/contributors

There are several ways you can help us:
* **Raise an [issue](https://github.com/epam/Wilma/issues).** Have you found something that does not work as expected? Let us know about it.
* **Suggest a feature by submitting an [issue](https://github.com/epam/Wilma/issues).** It's even better if you come up with a new feature and write us about it.
* **Write some code.** We would love to see pull requests to this tool. Feel free to contribute (send pull request) on GitHub.

#### Advised working environment
* OpenJDK 15 for V2.5.x
* IntelliJ IDEA Community Edition
* Gradle (included in source) and Git

#### Building with Gradle
The project can be built by following the instructions described [here](https://github.com/epam/Wilma/wiki/DEV,-Build-from-Scratch).
This way of build is recommended for contributors only, End-Users, please use the pre-built downloadable releases from [here](https://github.com/epam/Wilma/releases), or use the docker image.

[![CI](https://github.com/epam/Wilma/actions/workflows/main.yml/badge.svg)](https://github.com/epam/Wilma/actions/workflows/main.yml)

[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-white.svg)](https://sonarcloud.io/dashboard?id=epam%2FWilma)

## Detailed information
* Check the [Wiki](https://github.com/epam/Wilma/wiki) and [Issues](https://github.com/epam/Wilma/issues) link on GitHub
* Check further documentation at [http://epam.github.io/Wilma/](http://epam.github.io/Wilma/)

# License - GPLv3.0
Copyright since 2013 EPAM Systems

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
