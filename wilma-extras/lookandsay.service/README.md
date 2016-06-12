Wilma Example - Look-And-Say Service
====================================

Purpose of this example
---------------------------------------
This is a very basic example on configuring and using the External Service feature of Wilma.

This plugin implements an external service, that has a very basic and simple function:
Based on the [Look-And-Say sequence](https://en.wikipedia.org/wiki/Look-and-say_sequence),
when this plugin gets a number, it will generate the Nth next number in the sequence.

Implementation
---------------------------------------
Now let's see how it works.

First of all the `LookAndSayServiceInterceptor` class uses the external service extension point of Wilma: `ExternalWilmaService`.

+ `ExternalWilmaInterface` - to implement this interface you need to implement two methods.
  + The first (`getHandlers`) tells Wilma, what kind of external services are offered by the plug-in.
  See the method of what you need to give back in order to create a new Wilma endpoint.
  + The second method (`handleRequest`) is the entry point of the external service.
  In the method, you may detect that this service endpoint has 2 parameters, one of them is mandatory, the other is optional:
    + "number" that is used as starting number to calculate the Look-And-Feel sequence. This is mandatory.
    + "iterations" that is used as how steps we should move forward in the Look-And-Say sequence. Not mandatory, its default number is 1.
+ `InterceptorCore` - this class only has a single method, that method calculates the next number in the Look-And-Say sequence.

Important Settings
---------------------------------------
+ First of all, don't forget to move/upload the plugin jar into the `config/jar` folder of Wilma.
If you don't have the jar itself, compile it by using this command in the root folder of Wilma source: `gradlew build -b wilma-extras/lookandsay.service/build.gradle build`
and it will be built into the `wilma-extras/lookandsay.service/build` folder.
+ Don't forget to load the External Service (the jar) into Wilma. The easiest way is to use the sample xml configuration file, that you may find in `src/main/resources` folder.
Please note that in this configuration xml file the DialogDescriptors are disabled, still the interceptor part lives.
+ Don't forget to **enable** the interceptors in Wilma! Without that, the Look-And-Say plugin won't work at all.

How to call the Look-And-Say service endpoint of Wilma?
---------------------------------------
Note: In order to list the available services, use this call:
```
GET or POST http://localhost:1234/config/public/services/
```
See more information on using external service calls from Wilma plugins [here](https://github.com/epam/Wilma/wiki/Service-extensions-in-Plugins).
In case you configured well, you should get this kind of answer for the call above:
```
{
  "serviceMap": [
    {
      "LookAndSayServiceInterceptor/look-and-say-service": "com.epam.wilma.extras.lookandsayservice.LookAndSayServiceInterceptor"
    }
  ]
}
```
Also, in the example below, it is assumed that the default configuration is in use and Wilma is used on `localhost`,
therefore the Wilma `<standardExternalServiceUrl>` is: `http://localhost:1234/config/public/services/`.

Based on this, the Look-And-Say service is available here:

```
GET http://localhost:1234/config/public/services/LookAndSayServiceInterceptor/look-and-say-service?number=1&iterations=1
```

The "number" parameter is mandatory, without that, or by specifying an invalid number, an error response will be received.
This is the input number of the sequence calculation.

The "iterations" parameter is optional, if not given, or a wrong value is give, will be considered as 1.
Otherwise Look-And-Say sequence will be calculated as many times as specified in this value.

When the calculation is finished, the service will response with the result in the following JSON format:
```
{
  "number": "1",
  "iterations": "10",
  "serviceResult": "11131221133112132113212221",
  "length": "26"
}
```

Where "number" is the starting point, "iterations" are the number of iterations used,
"serviceResult" is the result of the calculation, and finally "length" is the length of the service result.

**Beware** that giving long "number" and/or giving big value for "iterations", the calculation may take long time.
To avoid very long response times, there is an internal limitation built in, and the service first guess the time of calculation,
and if it seems that will be too long, then you may get this answer:
```
{
  "serviceCalculationProblem": "Specified initial string and requested number of iterations would take unacceptable long time."
}
```

Have fun!
