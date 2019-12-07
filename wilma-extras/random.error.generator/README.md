Wilma Plugin Example - Random Error Generator
===============================

Purpose of this example
---------------------------------------
This plugin example was created to show how Wilma can act as a special kind of Chaos Monkey. See the original Chaos Monkey [tool here](https://github.com/Netflix/chaosmonkey), which tool is created by Netflix for AWS. 
So what kind of Chaos Monkey this example will show? This will simulate the following error situations:

 - E404 error will be received as response for every 5 requests, randomly
 - response timeout will be caused for every 10 requests, randomly
 - E500 error will be received as response for every 25 requests, randomly
 
Implementation
---------------------------------------
A single condition checker class is implemented, that takes care about all three error situations.
It expects one name/value parameter pair in the sub configuration file from the following possibilities: `E404`, `E500` and `TIMEOUT120SEC`.
Three dialog descriptors used within the stub configuration file to handle the 3 different error situation.
A simple `text/plain` type of responses will be generated for all cases, and in case of `TIMEOUT120SEC`, the response would be sent back only after a 120 sec delay.

Please beware that the randomness and the evaluation order will influence the probability of the error responses,
and to minimize the probability distortion,
the error cases listed in the dialog descriptors must be ordered by its expected frequency, and started with the lowest one -> 
in our case E500 should be at the front of the list.  

We used a fourth dialog descriptor that generates "normal" response always, and simulates the case when everything goes well (no error is generated and the service responds normally).

Build
-----
```
#run extras: Random Error Generator example
./gradlew -b wilma-extras/random.error.generator/build.gradle clean build
```
After a successful run you will find the wilma plug-in in folder: wilma-extras/random.error.generator/build/libs as a jar file.

How to Use it?
-----------------------------------------
- Ensure that the compiled randomErrorGenerator jar is available for your Wilma instance
- Configure Wilma to use it (see example configuration above)
- Pls don't forget to increase the `proxy.request.timeout` parameter of Wilma in its config file **before you start the Wilma instance itself**, 
  to a higher number than 120 secs, otherwise you will receive proxy request timeout because of the large delay in response.
  The suggested value is `proxy.request.timeout=125000`, that means 125 secs timeout for Wilma itself.

