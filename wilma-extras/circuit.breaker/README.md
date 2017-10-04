Wilma Example - Circuit Breaker
=============================

Purpose of this example
---------------------------------------
First of all, this is a very complex example, uses almost all features and extension points of Wilma.
If you just start with the examples, don't start with this one, as this is - again - a complex one.


Implementation
---------------------------------------

Important Settings
---------------------------------------
Don't forget to **enable** the interceptors! Without that, the Circuit Breaker won't work properly (no message will be cached at all).

REST Interface of Circuit Breaker plugin
---------------------------------------
Note: In order to list the available services, use this call:
```
GET or POST http://localhost:1234/config/public/services/
```
See more information on using external service calls from Wilma plugins [here](https://github.com/epam/Wilma/wiki/Service-extensions-in-Plugins).
Also, in the examples below, it is assumed that the default configuration is in use and Wilma is used on `localhost`,
therefore the Wilma `<standardExternalServiceUrl>` is: `http://localhost:1234/config/public/services/`.

