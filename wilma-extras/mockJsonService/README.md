Wilma Example - Mock JSON Service
====================================

Purpose of this example
---------------------------------------
This is an example of a fast method of mocking a full service in an easy way.

This plugin implements an external service, that has a very basic and simple function:
- It is possible to send a JSON request to the service by specifying an URL and a JSON message. This will be used as a mock for the request.
- In case a request arrives to Wilma that matches to the URL, then the specified JSON message will be used as response.
- Finally it is possible to clean all the received mocks.

This simple mechanism offers a basic but very powerful mocking mechanism - e.g. virtualizing a JSON service.

Implementation
---------------------------------------
Now let's see how it works.

First of all the `MockJsonServiceInterceptor` class uses the external service extension point of Wilma: `ExternalWilmaService`.
This ensures the Wilma plugin implementation, together with its subclass, the `MockJsonServiceCore` that handles 
- the GET (list stored mocks),
- the POST (add a new mock to the list),
- the DELETE (delete all mocks)
commands.

The `MockJsonServiceChecker` is a condition checker implementation, and checks if there is a mock for the specified request.
The `MockJsonServiceResponseGenerator` generates the mock answers, as necessary.

Important Settings
---------------------------------------
+ First of all, don't forget to move/upload the plugin jar into the `config/jar` folder of Wilma.
If you don't have the jar itself, compile Wilma, 
and the plugin jar will be built into the `wilma-extras/mockJsonService/build/lib` folder.
+ Don't forget to load the External Service (the jar) into Wilma. The easiest way is to use the sample json configuration file, that you may find in `src/main/resources` folder.
+ Don't forget to **enable** the interceptors in Wilma! Without that, the plugin won't work at all.

How to call the Mock JSON service endpoint of Wilma?
---------------------------------------
Note: In order to list the available services, use this call:
```
GET or POST http://localhost:1234/config/public/services/
```
See more information on using external service calls from Wilma plugins [here](https://github.com/epam/Wilma/wiki/Wilma-Extras:-Service-Endpoint-Extensions-in-Plugins).
In case you configured well, you should get this kind of answer for the call above:
```
{
  "serviceMap": [
    {
      "MockJsonServiceInterceptor/mock-json-service": "com.epam.wilma.extras.mockjsonservice.MockJsonServiceInterceptor"
    }
  ]
}
```
Also, in the example below, it is assumed that the default configuration is in use and Wilma is used on `localhost`,
therefore the Wilma `<standardExternalServiceUrl>` is: `http://localhost:1234/config/public/services/`.

Based on this, the Mock Json service is available here:

```
GET http://localhost:1234/config/public/services/MockJsonServiceInterceptor/mock-json-service
```

This will list the configured mocks.

To create a new mock just need to send a **POST** message to the same URL, with a JSON body, that has the following content:
```
{ 
    "add" : {
        "mockUrl": "http://test.vitrualized-service.com/a",
        "mockAnswer": "{ \"answer\": \"something-a\" }"
    }
}
```
Further parameters can ba added, like:
```
        "mockBodyContent": "anystrying"                             //checks if 'anystring' is in the body
        "mockJsonPath" : { "path" : "$.transactionType", "value": "OT" }   //checks if json "value" can be found at json "path" 
```
More than one parameter can be used, and a logical AND method will be used if there is more than one.

Also, the existing service can be saved and loaded as simple JSON file:
```
{ 
    "saveJson" : "resourcename"
}
```
And loaded of course:
```
{ 
    "loadJson" : "resourcename"
}
```
The last **POST** command is the save the mock set as standard WIlma configuration file:
```
{ 
    "saveWilmaConfiguration" : "resourcename",
    "groupName" : "name of the service and version"
}
```
This file can be loaded to Wilma as configuration, any time. Note that in this case not the Mock Json Service, but Wilma itself will use the mock configurations.

Finally, if you would like to start from the very beginning, just send a DELETE request to the Mock Json Service, and all the stored mocks will be deleted.

So easy! Have fun!
