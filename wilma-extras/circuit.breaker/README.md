Wilma Example - Circuit Breaker
=============================

Purpose of this example
---------------------------------------
The purpose of this example to implement a very simple Circuit Breaker. 

In sort, Circuit Breaker is a proxy, that detects if the communication is bad between the services, 
and in that case the Circuit Breaker is short-cutting the requests for a predefined duration of time.

Search for "Circuit Breaker" on the web (search for Circuit Breaker next to Microservices) for much more detailed information.

Implementation
---------------------------------------
The solution we selected fits to the approach Wilma uses, that means a single Wilma instance 
is able to act as Circuit Breaker for many services.
In our example we assume that we need to control 2 communication lines, that means we define 2 Circuit Breakers.

Each Circuit breaker has properties:

| Property name | Meaning | Example value |
|---------------|---------|---------------|
| identifier | This identifies the Circuit Breaker, use unique value. | "CB_1" |
| path | This is used as URL to identify the communication channel. | "http://service.to.call/" |
| timeoutInSec | When the Circuit Breaker is turned ON and become active (i.e. answers instead of the called service), after a certain timeout it will be turned OFF. This is the timeout that is used for this purpose.| "120" |
| successCodes | The listed response codes are considered as normal response codes. When Circuit Breaker detects a result code that is not in this list, that is considered as a communication error| "200,201,303" |
| maxErrorCount | This is the number of errors must be detected on the communication channel before the Circuit Breaker is turned ON. | "4" |

The actual information of a specific Circuit Breaker is stored in class: `CircuitBreakerInformation`.

To make the Circuit Breaker work, first of all we need to detect if the communication is wrong or not - this is implemented
 as interceptor of the response (see class `CircuitBreakerInterceptor`). 
This response interceptor
 * In case the request of the response fits to the specific Circuit Breaker (see identifier property), and that is inactive,
   * checks if the response is acceptable or not (see successCodes property). If not, increases the counter of the errors,
   * and if that error counter is getting higher than the limit (see maxErrorCount property) then turns the Circuit Breaker ON.
   
The request interceptor has other purpose:
 * In case the request fits to the specific Circuit Breaker (see identifier property), and that is active, then:
 * if its timeout is not elapsed,
    * then adds a new header to the request.
    * otherwise turns the Circuit Breaker OFF.   

The name of the new header is `Wilma-CircuitBreakerId`, and its value is the identifier of the specific Circuit Breaker.

By checking the existence of that new header during the evaluation of the conditions (see class `CircuitBreakerChecker`), 
we can decide if the request must be forwarded to the original target (= the Circuit Breaker is OFF, and the header is missing) 
or not (= the Circuit Breaker is ON, and the header is available). In this later case, a stub answer is generated.  

Please have a look at the example stub configuration file within `src/main/java/resources` folder, its name is `circuitBreakerStubConfigurationExample.xml`.
In that file you will find 2 Circuit Breakers, "CB_1" and "CB_2", for two services ( http://127.0.0.1/test1/ and http://127.0.0.1/test2/ ).

Important Settings
---------------------------------------
Don't forget to **enable** the interceptors! Without that, the Circuit Breaker won't work properly (the circuit will never be broken).

REST Interface of Circuit Breaker plugin
---------------------------------------
Note: In order to list the available services, use this call:
```
GET or POST http://localhost:1234/config/public/services/
```
See more information on using external service calls from Wilma plugins [here](https://github.com/epam/Wilma/wiki/Service-extensions-in-Plugins).
Also, in the examples below, it is assumed that the default configuration is in use and Wilma is used on `localhost`,
therefore the Wilma `<standardExternalServiceUrl>` is: `http://localhost:1234/config/public/services/`.

The internal service of the Circuit Breaker plug-in is available here  (see class `CircuitBreakerService`):
```
GET http://localhost:1234/config/public/services/CircuitBreakerInterceptor/circuit-breaker
```
Of course this link will olny work in case Wilma is running and the Circuit-Breaker plug-in is properly configured in it.
In the response all activated circuit breaker will be listed, together its configuration and the actual status of the Circuit Breaker itself.

Please note that the response will be empty in case no any message is arrived to Wilma, 
as the list of the Circuit Breakers will be initiated during the interception of the first request.

To make the Circuit Breaker configuration changeable, it is possible to update the stub configuration of Wilma during run time,
and then call this endpoint: 

```
DELETE http://localhost:1234/config/public/services/CircuitBreakerInterceptor/circuit-breaker
```
in order to clean-up the already loaded Circuit Breakers, and during the first request arrives to Wilma, 
the configuration of the Circuit Breakers will be reloaded.
